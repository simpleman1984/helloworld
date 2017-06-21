/**
 * UI 视图相关的操作
 */
$.fn.scrollSmooth = function(scrollHeight, duration) {
    var $el = this;
    var el  = $el[0];
    var startPosition = el.scrollTop;
    var delta = scrollHeight  - startPosition;
    var startTime = Date.now();
    function scroll() {
        var fraction = Math.min(1, (Date.now() - startTime) / duration);
        el.scrollTop = delta * fraction + startPosition;
        if(fraction < 1) {
            setTimeout(scroll, 10);
        }
    }
    scroll();
};

$.fn.goSmooth = function(height, duration) {
    var $el = this;
    var startPosition = 1 * $el.css('margin-bottom').replace('px', '');
    var delta = height  - startPosition;
    var startTime = Date.now();
    function scroll() {
        var fraction = Math.min(1, (Date.now() - startTime) / duration);
        $el.css('margin-bottom', delta * fraction + startPosition);
        if(fraction < 1) {
            setTimeout(scroll, 10);
        }
    }
    scroll();
};

function Queue() {};
Queue.prototype = {
    add: function(el) {
        if (this._last) {
            this._last = this._last._next = { //_last是不断变的
                el: el,
                _next: null //设置_last属性表示最后一个元素，并且让新增元素成为它的一个属性值
            }
        } else {
            this._last = this._first = { //我们要设置一个_first属性表示第一个元素
                el: el,
                _next: null
            }
        }
        return this;
    }
}

function copyQueue(p) {
    var queue = new Queue;
    for (var i = 0; i < p.length; i++) {
        queue.add(p[i]);
    }
    return queue;
};

function activeInput() {
    $('.box_ft').find('.input-wrapper').addClass('J_inputWrapper');
}

function deactiveInput() {
    $('.box_ft').find('.input-wrapper').removeClass('J_inputWrapper');
}

/**
 * 显示下方可选中操作
 * @param choice
 * @param delay
 */
function showChoice(choice, delay) {
    //$('.J_noticeInput').hide();

    if (delay === null) {
        delay = 100;
    }
    if (!choice) {
        choice = '0';
    }

    setTimeout(function() {
        $('.J_choiceWrapper').addClass('opened').find('.J_choice').removeClass('choosen').hide();
        $('.J_inputWrapper').addClass('opened');
        var $choiceUl = $('.J_choiceWrapper').find('.J_choice').filter('[data-choice="' + choice + '"]');
        var cH = $choiceUl.addClass('choosen').show().height();
        var ftH = $('.box_ft').height();
        var sOH = $('#chatContent').height();

        $('.box_bd').goSmooth(cH, 100);
        $('.J_scrollContent').scrollSmooth(ftH + sOH, 300);

    }, delay);
}

function hideChoice() {
    $('.box_bd').goSmooth(0, 100);
    $('.J_inputWrapper').removeClass('opened');
    $('.J_choiceWrapper').removeClass('opened');
}

/**
 * 插入聊天内容
 */
function showDialog(dialog, cb) {
    var $chat = $('#chatContent');

    // 显示对话的时候，菜单栏不可点
    deactiveInput();

    var message = copyQueue(dialog)._first;
    var tpl = doT.template($('#messageTpl').html());

    function loop(delay) {
        // speed
        if (delay == undefined) {
            // random delay between messages
            delay = Math.random() * 1000 + 600;
            //delay = Math.random() * 50 + 50;
        }

        var timeout = setTimeout(function() {
            if (message) {
                // 显示 message
                var messageHtml = tpl([message.el]);
                $chat.append(messageHtml);

                // 自动滚动
                var viewH = $('.J_scrollContent').height();
                var contentH = $chat.height();
                if (contentH > viewH) {
                    $('.J_scrollContent').scrollSmooth(contentH - viewH + 16, 300)
                }

                // 执行附加效果
                if (message.el.flag) {
                    var flag = message.el.flag;

                    switch (flag) {
                        case 'animate-tour':
                            playTour();
                            break;
                        default:
                            break;
                    }
                }

                // 特别语句的特殊delay
                if(message.el.pause != undefined) {
                    loop(message.el.pause);
                } else {
                    loop();
                }

                // 指向下一句
                message = message._next;

            } else {
                activeInput();
                clearTimeout(timeout);
                cb && cb();
            }
        }, delay);
    };

    loop(0);
};

/**
 * 初始化单页面切换器
 **/
var pageManager = {
    $container: $('#container'),
    _pageStack: [],
    _configs: [],
    _pageAppend: function(){},
    _defaultPage: null,
    _pageIndex: 1,
    setDefault: function (defaultPage) {
        this._defaultPage = this._find('name', defaultPage);
        return this;
    },
    setPageAppend: function (pageAppend) {
        this._pageAppend = pageAppend;
        return this;
    },
    init: function () {
        var self = this;

        $(window).on('hashchange', function () {
            var state = history.state || {};
            var url = location.hash.indexOf('#') === 0 ? location.hash : '#';
            var page = self._find('url', url) || self._defaultPage;
            if (state._pageIndex <= self._pageIndex || self._findInStack(url)) {
                self._back(page);
            } else {
                self._go(page);
            }
        });

        if (history.state && history.state._pageIndex) {
            this._pageIndex = history.state._pageIndex;
        }

        this._pageIndex--;

        var url = location.hash.indexOf('#') === 0 ? location.hash : '#';
        var page = self._find('url', url) || self._defaultPage;
        this._go(page);
        return this;
    },
    push: function (config) {
        this._configs.push(config);
        return this;
    },
    go: function (to) {
        var config = this._find('name', to);
        if (!config) {
            return;
        }
        location.hash = config.url;
    },
    _go: function (config) {
        this._pageIndex ++;

        history.replaceState && history.replaceState({_pageIndex: this._pageIndex}, '', location.href);

        var html = $(config.template).html();
        var $html = $(html).addClass('slideIn').addClass(config.name);
        $html.on('animationend webkitAnimationEnd', function(){
            $html.removeClass('slideIn').addClass('js_show');
        });
        this.$container.append($html);
        this._pageAppend.call(this, $html);
        this._pageStack.push({
            config: config,
            dom: $html
        });

        if (!config.isBind) {
            this._bind(config);
        }

        return this;
    },
    back: function () {
        history.back();
    },
    _back: function (config) {
        this._pageIndex --;

        var stack = this._pageStack.pop();
        if (!stack) {
            return;
        }

        var url = location.hash.indexOf('#') === 0 ? location.hash : '#';
        var found = this._findInStack(url);
        if (!found) {
            var html = $(config.template).html();
            var $html = $(html).addClass('js_show').addClass(config.name);
            $html.insertBefore(stack.dom);

            if (!config.isBind) {
                this._bind(config);
            }

            this._pageStack.push({
                config: config,
                dom: $html
            });
        }

        stack.dom.addClass('slideOut').on('animationend webkitAnimationEnd', function () {
            stack.dom.remove();
        });

        return this;
    },
    _findInStack: function (url) {
        var found = null;
        for(var i = 0, len = this._pageStack.length; i < len; i++){
            var stack = this._pageStack[i];
            if (stack.config.url === url) {
                found = stack;
                break;
            }
        }
        return found;
    },
    _find: function (key, value) {
        var page = null;
        for (var i = 0, len = this._configs.length; i < len; i++) {
            if (this._configs[i][key] === value) {
                page = this._configs[i];
                break;
            }
        }
        return page;
    },
    _bind: function (page) {
        var events = page.events || {};
        for (var t in events) {
            for (var type in events[t]) {
                this.$container.on(type, t, events[t][type]);
            }
        }
        page.isBind = true;
    }
};

function setPageManager(){
    var pages = {}, tpls = $('script[type="text/html"]');
    var winH = $(window).height();

    for (var i = 0, len = tpls.length; i < len; ++i) {
        var tpl = tpls[i], name = tpl.id.replace(/tpl_/, '');
        pages[name] = {
            name: name,
            url: '#' + name,
            template: '#' + tpl.id
        };
    }
    pages.home.url = '#';

    for (var page in pages) {
        pageManager.push(pages[page]);
    }
    pageManager
        .setPageAppend(function($html){
            var $foot = $html.find('.page__ft');
            if($foot.length < 1) return;

            if($foot.position().top + $foot.height() < winH){
                $foot.addClass('j_bottom');
            }else{
                $foot.removeClass('j_bottom');
            }
        })
        .setDefault('home')
        .init();
}
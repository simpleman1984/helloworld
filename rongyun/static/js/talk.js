var userId = queryString("u");
var DBName= "TalkDB_" + userId;
var Store_Msg = "Msg";
var me = {};
var _members = {};
var _dialog = {};
//数据库检查完毕，开始加载数据
checkDatabaseStatus(DBName,Store_Msg)
    //加载当前登录人员信息
    .then(function () {
        return $.ajax({"url":"json/user.json"});
    })
    .then(function(user){
        //当前用户id，临时写死；
        user.gid = userId;
        _members.me = user;
    })
    //加载好友列表
    .then(function(){
        return $.ajax({"url":"json/friends.json"});
    })
    .then(function(friends){
        $.extend(_members,friends);
    })
    .then(Start)
    .then(initIM);

/**
 * 准备完毕，开始加载数据~~~
 * @constructor
 */
function Start() {
    //设置单页面
    setPageManager();

    //加载历史消息
    IndexDB.getAllData(DBName, Store_Msg).then(function(allMsg){
        console.info(allMsg);

        //读取保存的历史消息
        var hisMsg = [];
        for(var index in allMsg){
            var msg = allMsg[index];
            hisMsg.push({
                id:msg.id ,
                type: msg.type ? msg.type : 'plain',
                author: _members[msg.sender == _members.me.gid ? "me" : msg.sender],
                content: msg.content,
                pause: 1,
            });
        }

        //展示默认的引导对话
        showDialog(hisMsg, function() {
            //$('.J_noticeInput').show();
        });
    },function (e) {
        console.error(e);
    });

    //回车发送消息；清空输入框，发送输入情况
    $("#content").keyup(function(event){
        if(event.keyCode  == 13)
        {
            var content = $(this).val();
            //发送消息到远程MQ
            sendMessage("1",content).then(function(){
                showDialog([{
                    type: 'plain',
                    author: _members.me,
                    content: content,
                    pause: 500,
                }]);
            });
            $(this).val("");
        }
    });

    // 不同选项点击触发不同的对话和下级选项(展示禁用这些自动回复的代码)
    $('.box_ft').on('click', '.J_choice .J_liNextDisabled', function(e) {
        e.preventDefault();
        var $choice = $(this);
        var dialogId = $choice.attr('data-target-dialog');
        var choiceId = $choice.attr('data-target-choice');
        var continueDialog = ($choice.attr('data-continue') !== 'false');

        // if ($choice.hasClass('disabled')) {
        // 	return;
        // }
        $('.J_mainChoice').find('.J_liNext[data-target-dialog="' + dialogId + '"]').addClass('disabled');

        if (!choiceId) {
            choiceId = '0';
        }

        hideChoice();
        clearTimeout(window.waitUser);

        showDialog(_dialog['d' + dialogId], function() {
            if (continueDialog) {
                showChoice(choiceId, 500);
                // 用户若干秒没操作的话，提示文案
                window.waitUser = setTimeout(function() {
                    var random = Math.floor( (Math.random() * 3) + 1);
                    showDialog(_dialog['dr_' + random]);
                }, 30000);
            }
        });

        if ( !$('.J_mainChoice').find('.J_liNext').not('.disabled') ) {
            clearTimeout(window.waitUser);
        }
    });

    //显示或者隐藏下方可操作菜单
    $(document).on('click', '.J_inputWrapper > img', function() {
        var choosen = $('.J_choice').filter('.choosen').attr('data-choice');
        if ($('.J_choiceWrapper').hasClass('opened')) {
            hideChoice();
        } else {
            showChoice(choosen, 0);
        }
    });

    //显示红包详情
    $(document).on('click', '.hongbao', function(e){
        var msgUid = $(this).parents(".clearfix").attr("uuid");
        getMessage(DBName,Store_Msg,msgUid).then(function(msg){
            var sender = msg.sender;
            //如果发送者为当前登录人员，则进入自己发送红包状态查看页面。
            if(sender == userId)
            {
                location.hash = "#sender_hongbao_info";
                $("#container").show();
            }
            //以下为我接收到的红包
            else
            {
                $.MsgUid = msgUid;
                //红包已打开
                if(msg.status == "Opened")
                {
                    location.hash = "#receiver_query_hongbao_info";
                }
                //进入红包打开页面
                else
                {
                    location.hash = "#receiver_snatch_hongbao_info";
                }
                $("#container").show();
            }
        });
    });

    //退出到首页
    $(document).on('click','.back-home',home);

};

/**
 * 抢红包界面
 */
function openHongBao(){
    updateHongBaoStatus(DBName,Store_Msg,$.MsgUid,"Opened").then(function(){
        location.hash = "#receiver_query_hongbao_info";
    });
}

/**
 * 进入红包输入界面
 */
function startSend(){
    location.hash = "#start_send";
    $("#container").show();
}
/**
 * 发送红包
 */
function sendRedPackets(){
    $("#container").hide();
    //发送红包，更新UI
    var message = {};
    message.content = "红包";
    message.type   = "hongbao";

    sendMessage("1",message).then(function(_message){
        showDialog([{
            id:_message.id,
            type: message.type,
            author: _members.me,
            content: "hongbao.jpg",
            pause: 500,
        }],function () {
            hideChoice();
        });
    });
}

/**
 * 打开红包
 */
function openHongbao(){
    updateHongBaoStatus(DBName,Store_Msg,uuid,status).then(function(){

    });
};



/**
 * 跳转到首页
 */
function home(){
    $("#container").hide();
    location.hash = "#";
}

var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];

var packet = require("./packet");
var handler = require("./handler");

function generateMixed(n) {
    var res = "";
    for(var i = 0; i < n ; i ++) {
        var id = Math.ceil(Math.random()*35);
        res += chars[id];
    }
    return res;
}
/**
 * 具体接入的某个客户端(可以为老采集器，也可以为新采集器)
 * @constructor
 */
function Client(sock){
    /**
     * 客户端绑定socks对象
     */
    this.sock = sock;
    /**
     * 本次数据接收，用的什么协议。
     * @type {string}
     */
    this.protocol = "";
    /**
     * 客户端唯一id
     * @param data
     */
    this.id = generateMixed(32);

    /**
     * 发送数据包<br/>
     * packet 数据包
     */
    this.send = function(packet){
        this.sock.write(Buffer.from(packet,"hex"));
    };

    /**
     * 接收数据，解析数据包；
     * @param data
     */
    this.onData = function(data){
        var pack = packet.extract(data.toString("hex"));
        console.info("接收完整数据包为:"+data.toString("hex"));
        //开始处理对应的数据包
        handler.handle.call(this,pack.C,pack.A,pack.AFN,pack.SEQ,pack.Data,pack.Aux);
        // sock.write('You said "' + data + '"');
    };

    this.onClose = function(){
    };

};
exports.Client = Client;

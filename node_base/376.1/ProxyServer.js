/**
 * Created by xuaihua on 2017/1/24.
 */
var net = require('net');
var Client = require("./Client").Client;
var _ = require("./underscore");

var sockArray = [];
/**
 * 客户端连接接入回调
 * @param sock
 */
var sockJoinCallback = function(sock){
    var client = new Client(sock);
    // We have a connection - a socket object is assigned to the connection automatically
    console.log('CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);

    //接收到新数据
    sock.on('data',function(data){
        console.log('Receive DATA ' + sock.remoteAddress,data);
        client.onData(data);
    });

    //sock关闭
    sock.on('close',function(){console.info(sockArray.length)
        console.log('CLOSED: ' + sock.remoteAddress +' '+ sock.remotePort);
        client.onClose();
        //移除客户端
        sockArray.splice(_.findIndex(sockArray, { id: client.id }), 1);console.info(sockArray.length)
    });

    //添加客户端
    sockArray.push(client);
};
//设置服务器接入回调，并开启监听端口
net.createServer().on("connection",sockJoinCallback).listen(5432,"192.168.0.138");
net.createServer().on("connection",sockJoinCallback).listen(5442,"192.168.0.138");

var mqtt = require('mqtt')
var client  = mqtt.connect('mqtt://127.0.0.1:1883')
 

//mqtt 客户端内容
//https://www.npmjs.com/package/mqtt

client.on('connect', function () {
  client.subscribe('noticeClientPush')
  //client.publish('presence', 'Hello mqtt')
})
 
client.on('message', function (topic, message) {
  // message is Buffer 
  console.log(message.toString())
  //client.end()
})
function queryString(name){
    var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i"));
    if(result == null || result.length < 1){
        return "";
    }
    return result[1];
};

/**
 * 检查数据库状态~
 */
function checkDatabaseStatus(dbName,storeName){
    //首次登录，检查数据库的创建情况
    var opts = {
        dbName: dbName,
        storeName: storeName,
        keyOptions: {
            keyPath: 'id', //primary key
            autoIncrement: false, //auto increment
        },
        index: [  //index, use for query the data
            {
                indexName: 'idIndex',
                indexKey: 'id',
                indexOptions: { unqiue: false, mulitEntry: false }
            }
        ]
    };

    return new Promise(function (resolve, reject) {
        IndexDB.getDB(DBName).then(function(db){
            console.info(db);
            var objectStoreNames = db.objectStoreNames ;
            if(!objectStoreNames.contains(Store_Msg)){
                IndexDB.createStore(opts).then(function(){
                    console.log('success');
                },function(e){console.info(e,"123");});
            }
            //db.close();

            //数据库初始化完毕，开始读取数据~
            resolve(db);
        });
    });
}

/**
 * 保存消息
 * @param dbName
 * @param storeName
 * @returns {*}
 */
function storeMessage(dbName,storeName,message){
    var _message = {
        content:message.content.content || message.content ,
        sender: message.sender || message.senderUserId,
        id:message.messageUId,
        receiveTime:message.receivedTime,
        sendTime:message.sentTime,
        targetId:message.targetId,
        type:message.type||"plain"
    }
    return IndexDB.addOneData(dbName,storeName,_message);
}

/**
 * 获取当前消息
 * @param dbName
 * @param storeName
 * @param uuid
 */
function getMessage(dbName,storeName,uuid)
{
    return IndexDB.getDataByIndex(dbName,storeName,"idIndex",uuid);
}

/**
 * 更新当前红包的状态（未抢-默认；已抢；已被人抢完；已过期；）
 * @param dbName
 * @param storeName
 * @param uuid
 * @param status
 */
function updateHongBaoStatus(dbName,storeName,uuid,status){
    return getMessage(dbName,storeName,uuid)
        .then(function(data){
            data.status = status;
            return data;
        })
        .then(function(newData){
            return new Promise(function(resolve,reject){
                IndexDB.putOneData(dbName,storeName,newData).then(resolve,resolve);
            })
        });
}
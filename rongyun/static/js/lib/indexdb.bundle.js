(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
        typeof define === 'function' && define.amd ? define(factory) :
            (global.IndexDB = factory());
}(this, (function () { 'use strict';

    var showError = (function (msg) {
        console.error("[indexdb_api]: " + msg);
    });

//cache
    var DBs = {};

    /**
     * create a new db
     */
    function createDB(dbName) {
        var version = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : new Date().getTime();

        return new Promise(function (resolve, reject) {
            var dbConnect = window.indexedDB.open(dbName, version);
            dbConnect.onsuccess = function (event) {
                //save the opened db
                DBs[dbName] = event.target.result;
                resolve(event.target.result);
            };
            dbConnect.onerror = function (event) {
                showError(event.target.error.message);
                reject(event.target.error);
            };
        });
    }

    /**
     * delete a existed db
     */
    function deleteDB(dbName) {
        return new Promise(function (resolve, reject) {
            var dbConnect = window.indexedDB.deleteDatabase(dbName);
            dbConnect.onsuccess = function () {
                resolve();
            };
            dbConnect.onerror = function (event) {
                showError(event.target.error.message);
                reject(event.target.error);
            };
        });
    }

    /**
     * open an existed db
     */
    function getDB(dbName) {

        return new Promise(function (resolve, reject) {
            //get db from cache
            if (DBs[dbName]) {
                resolve(DBs[dbName]);
                return;
            }

            //get db from connect
            createDB(dbName).then(function (db) {
                resolve(db);
            }).catch(function (error) {
                reject(error);
            });
        });
    }

    /**
     * get all dbnames
     * @return {[type]} [description]
     */
    function getDBNames() {
        return new Promise(function (resolve, reject) {
            if (!window.indexedDB.webkitGetDatabaseNames) {
                showError('your browser doesn\'t support indexedDB.webkitGetDatabaseNames');
                resolve([]);
                return;
            }
            var get_dbNames_req = window.indexedDB.webkitGetDatabaseNames();

            get_dbNames_req.onsuccess = function (event) {
                var dbNames = Array.prototype.slice.call(event.target.result);
                resolve(dbNames);
            };

            get_dbNames_req.onerror = function (event) {
                showError(event.target.error.message);
                reject(event.target.error);
            };
        });
    }



    var db = Object.freeze({
        DBs: DBs,
        getDBNames: getDBNames,
        createDB: createDB,
        getDB: getDB,
        deleteDB: deleteDB
    });

    /**
     * [getAllData description]
     * @param  {[type]} store [description]
     * @return {[type]}       [description]
     */
    function getAllData(dbName, storeName) {

        return new Promise(function (resolve, reject) {
            getDB(dbName).then(function (db) {
                if (!isStoreNameCorrect(db, storeName)) {
                    reject();
                }

                var tx = db.transaction(storeName, 'readwrite');
                var store = tx.objectStore(storeName);

                tx.oncomplete = function () {
                    resolve(dataArr);
                };

                var req = store.openCursor(),
                    dataArr = [];

                req.onsuccess = function (event) {
                    var cursor = event.target.result;
                    if (cursor) {
                        dataArr.push(cursor.value);
                        cursor.continue();
                    }
                };

                req.onerror = function (event) {
                    showError(event.target.error.message);
                    reject(event.target.error);
                };
            }).catch(function (error) {
                reject(error);
            });
        });
    }

    /**
     * [getDataByIndex description]
     * @param  {[type]} store     [description]
     * @param  {[type]} indexName [description]
     * @param  {[type]} value     [description]
     * @return {[type]}           [description]
     */
    function getDataByIndex(dbName, storeName, indexName, value) {
        return new Promise(function (resolve, reject) {
            getDB(dbName).then(function (db) {
                if (!isStoreNameCorrect(db, storeName)) {
                    reject();
                }

                var tx = db.transaction(storeName, 'readwrite');
                var store = tx.objectStore(storeName);

                tx.oncomplete = function () {
                    resolve(data);
                };

                var index = store.index(indexName);
                var req = index.get(value);
                var data = void 0;

                req.onsuccess = function (event) {
                    data = event.target.result;
                };

                req.onerror = function (event) {
                    showError(event.target.error.message);
                    reject(event.target.error);
                };
            }).catch(function (error) {
                reject(error);
            });
        });
    }

    /**
     * 用主键获取指定对象库的范围数据
     */
    function getRangeDataByPrimaryKey(dbName, storeName, start, end) {
        return new Promise(function (resolve, reject) {
            getDB(dbName).then(function (db) {
                if (!isStoreNameCorrect(db, storeName)) {
                    reject();
                }

                var tx = db.transaction(storeName, 'readwrite');
                var store = tx.objectStore(storeName);

                tx.oncomplete = function () {
                    resolve(dataArr);
                };

                var range = IDBKeyRange.bound(start, end),
                    dataArr = [],
                    req = store.openCursor(range);

                req.onsuccess = function (event) {
                    var cursor = event.target.result;
                    if (cursor) {
                        dataArr.push(cursor.value);
                        cursor.continue();
                    }
                };

                req.onerror = function (event) {
                    showError(event.target.error.message);
                    reject(event.target.error);
                };
            }).catch(function (error) {
                reject(error);
            });
        });
    }

    /**
     * addOneData
     */

    function addOneData(dbName, storeName, data) {
        return new Promise(function (resolve, reject) {
            getDB(dbName).then(function (db) {
                if (!isStoreNameCorrect(db, storeName)) {
                    reject();
                }

                var tx = db.transaction(storeName, 'readwrite');
                var store = tx.objectStore(storeName);

                tx.oncomplete = function () {
                    resolve(count);
                };

                var add_data_req = store.add(data);
                var count = void 0;

                add_data_req.onsuccess = function (event) {
                    //event.target.result is the count of the data
                    count = event.target.result;
                };
                add_data_req.onerror = function (event) {
                    showError(event.target.error.message);
                    reject(event.target.error);
                };
            }).catch(function (error) {
                reject(error);
            });
        });
    }

    /**
     * update a data accoring to the primary key
     * you can use putOneData to add a data, when the primary is the same, then putOneData will update the old data
     */
    function putOneData(dbName, storeName, data) {
        return new Promise(function (resolve, reject) {
            getDB(dbName).then(function (db) {
                if (!isStoreNameCorrect(db, storeName)) {
                    reject();
                }

                var tx = db.transaction(storeName, 'readwrite');
                var store = tx.objectStore(storeName);

                tx.oncomplete = function () {
                    resolve(count);
                };

                var put_data_req = store.put(data);
                var count = void 0;

                put_data_req.onsuccess = function (event) {
                    count = event.target.result;
                };
                put_data_req.onerror = function (event) {
                    showError(event.target.error.message);
                    reject(event.target.error);
                };
            }).catch(function (error) {
                reject(error);
            });
        });
    }

    /**
     * 按主键删除数据
     */
    function deleteDataByPrimaKey(dbName, storeName, primaryKeyValue) {
        return new Promise(function (resolve, reject) {
            getDB(dbName).then(function (db) {
                if (!isStoreNameCorrect(db, storeName)) {
                    reject();
                }

                var tx = db.transaction(storeName, 'readwrite');
                var store = tx.objectStore(storeName);

                tx.oncomplete = function () {
                    resolve(true);
                };

                store.delete(primaryKeyValue);
            }).catch(function (error) {
                reject(error);
            });
        });
    }

    function isStoreNameCorrect(db, storeName) {
        if (db.objectStoreNames.contains(storeName)) {
            return true;
        } else {
            showError('store ' + storeName + ' doesn\'t existes');
            return false;
        }
    }



    var data = Object.freeze({
        getAllData: getAllData,
        getDataByIndex: getDataByIndex,
        getRangeDataByPrimaryKey: getRangeDataByPrimaryKey,
        addOneData: addOneData,
        putOneData: putOneData,
        deleteDataByPrimaKey: deleteDataByPrimaKey
    });

    /**
     * create a new store in an existed db
     */
    function createStore(_ref) {
        var _ref$dbName = _ref.dbName,
            dbName = _ref$dbName === undefined ? null : _ref$dbName,
            _ref$storeName = _ref.storeName,
            storeName = _ref$storeName === undefined ? null : _ref$storeName,
            _ref$version = _ref.version,
            version = _ref$version === undefined ? new Date().getTime() : _ref$version,
            _ref$keyOptions = _ref.keyOptions,
            keyOptions = _ref$keyOptions === undefined ? {} : _ref$keyOptions,
            _ref$index = _ref.index,
            index = _ref$index === undefined ? [] : _ref$index;

        if (!dbName || !storeName) {
            showError('dbName and storeName are both required');
            return false;
        }

        return new Promise(function (resolve, reject) {

            //close this db and delete form cache
            if (DBs[dbName]) {
                DBs[dbName].close();
                delete DBs[dbName];
            }

            var dbConnect = window.indexedDB.open(dbName, version);
            var store = void 0;

            //only in onupgradeneeded event , you can create store
            dbConnect.onupgradeneeded = function (event) {
                var db = event.target.result;
                //cache this db
                DBs[dbName] = db;
                if (db.objectStoreNames.contains(storeName)) {
                    //是否已存在该对象库
                    showError('storeName ' + storeName + ' has existed in db ' + dbName);
                    reject();
                } else {
                    store = db.createObjectStore(storeName, keyOptions); //创建对象库
                    for (var i in index) {
                        //创建索引
                        store.createIndex(index[i].indexName, index[i].indexKey, index[i].indexOptions);
                    }
                }
            };

            //wait for success to resolve
            dbConnect.onsuccess = function () {
                resolve(store);
            };

            dbConnect.onerror = function (event) {
                showError(event.target.error.message);
                reject(event.target.error);
            };
        });
    }

    /**
     * [getStore description]
     * @param  {[type]} dbName    [description]
     * @param  {[type]} storeName [description]
     * @return {[type]}           [description]
     */
    function getStore(dbName, storeName) {

        if (!dbName || !storeName) {
            showError('dbName and storeName are both required');
            return false;
        }

        return new Promise(function (resolve, reject) {
            getDB(dbName).then(function (db) {
                if (db instanceof IDBDatabase && db.objectStoreNames.contains(storeName)) {
                    var tx = db.transaction(storeName, 'readwrite');
                    var store = tx.objectStore(storeName);
                    resolve(store);
                    //  if do nothing, transaction will auto close
                    // can not resolve in oncomplete event, that means the transaction is close, nothing can be done
                    // tx.oncomplete = function() {
                    //
                    // };
                } else {
                    showError('store ' + storeName + ' doesn\'t existes in ' + dbName);
                    reject();
                }
            }).catch(function (error) {
                reject(error);
            });
        });
    }

    /**
     * [deleteStore description]
     * @param  {[type]} dbName       [description]
     * @param  {[type]} storeName    [description]
     * @param  {Date}   [version=new Date(]        [description]
     * @return {[type]}              [description]
     */
    function deleteStore() {
        var dbName = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : null;
        var storeName = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : null;
        var version = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : new Date().getTime();

        if (!dbName || !storeName) {
            showError('dbName and storeName are both required');
            return false;
        }

        return new Promise(function (resolve, reject) {

            //close this db and delete form cache
            if (DBs[dbName]) {
                DBs[dbName].close();
                delete DBs[dbName];
            }

            var dbConnect = window.indexedDB.open(dbName, version);
            var db = void 0;
            //only in onupgradeneeded event , you can delete store
            dbConnect.onupgradeneeded = function (event) {
                db = event.target.result;
                //cache this db
                DBs[dbName] = db;
                if (db.objectStoreNames.contains(storeName)) {
                    db.deleteObjectStore(storeName);
                } else {
                    showError('storeName ' + storeName + ' has existed in db ' + dbName);
                    reject();
                }
            };

            //wait for success to resolve
            dbConnect.onsuccess = function () {
                resolve(db);
            };

            dbConnect.onerror = function (event) {
                showError(event.target.error.message);
                reject(event.target.error);
            };
        });
    }

    /**
     * [getStoreCount description]
     * @param  {[type]} db        [description]
     * @param  {[type]} storeName [description]
     * @return {[type]}           [description]
     */
    function getStoreCount(dbName, storeName) {

        if (!dbName || !storeName) {
            showError('dbName and storeName are both required');
            return false;
        }

        return new Promise(function (resolve, reject) {
            getDB(dbName).then(function (db) {
                if (db instanceof IDBDatabase && db.objectStoreNames.contains(storeName)) {
                    var tx = db.transaction(storeName, 'readwrite');
                    var store = tx.objectStore(storeName);
                    var count = void 0;

                    tx.oncomplete = function () {
                        resolve(count);
                    };

                    //get count in transaction, you should create transaction before do anything to store
                    var store_count_req = store.count();
                    store_count_req.onsuccess = function (event) {
                        count = event.target.result;
                    };
                    store_count_req.onerror = function (event) {
                        showError(event.target.error.message);
                        reject(event.target.error);
                    };
                } else {
                    showError('store ' + storeName + ' doesn\'t existes in ' + dbName);
                    reject();
                }
            }).catch(function (error) {
                reject(error);
            });
        });
    }

    function clearStore(store) {
        if (store instanceof IDBObjectStore) {
            store.clear();
            return true;
        } else {
            return false;
        }
    }



    var store = Object.freeze({
        getStore: getStore,
        createStore: createStore,
        getStoreCount: getStoreCount,
        deleteStore: deleteStore,
        clearStore: clearStore
    });

    var _extends = Object.assign || function (target) {
            for (var i = 1; i < arguments.length; i++) {
                var source = arguments[i];

                for (var key in source) {
                    if (Object.prototype.hasOwnProperty.call(source, key)) {
                        target[key] = source[key];
                    }
                }
            }

            return target;
        };

    if (!window.indexedDB) {
        showError('Your browser doesn\'t support indexedDB.');
    }

    var index = _extends({}, db, data, store);

    return index;

})));
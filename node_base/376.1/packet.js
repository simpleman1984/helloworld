//http://stackoverflow.com/questions/40353000/adding-two-binary-and-returning-binary-in-javascript
//https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Bitwise_Operators

//终端登录
// 上行报文(终端发出)：C[C9]A[00 02 34 12 00]AFN[02]SEQ[71]DATA[00 00 01 00]CS[85]
// 68 32 00 32 00 68 C9 00 02 34 12 00 02 71 00 00 01 00 85 16

// 下行报文(主站发出)：C[0B]A[00 02 34 12 00]AFN[00]SEQ[61]DATA[00 00 01 00]CS[B5]
// 68 32 00 32 00 68 0B 00 02 34 12 00 00 61 00 00 01 00 B5 16

//终端心跳包
// 上行报文(终端发出)：C[C9]A[00 02 34 12 00]AFN[02]SEQ[71]DATA[00 00 04 00]CS[88]
// 68 32 00 32 00 68 C9 00 02 34 12 00 02 71 00 00 04 00 88 16

// 下行报文(主站发出)：C[0B]A[00 02 34 12 00]AFN[00]SEQ[61]DATA[00 00 01 00]CS[B5]
// 68 32 00 32 00 68 0B 00 02 34 12 00 00 61 00 00 01 00 B5 16

//复位终端
// 下行报文(主站发出)：C[41]A[00 02 34 12 04]AFN[01]SEQ[00]DATA[***]CS[19]
// 68 8A 00 8A 00 68 41 00 02 34 12 04 01 F0 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 28 53 13 07 05 19 16
// 上行报文(终端发出)：C[A0]A[00 02 34 12 04]AFN[00]SEQ[00]DATA[***]CS[19]
// 68 52 00 52 00 68 A0 00 02 34 12 04 00 E0 00 00 01 00 11 00 00 27 52 13 07 05 76 16 (蓝色字体表示p0,F1 全部确认)

//设置参数

//68H-------68
//L---------32 00-----00110010 00000000
//L---------32 00-----00110010 00000000
//68H
//C------------------C9（1个字节）
//A------------------00 02 34 12 00(5字节，A1：2字节“行政区划码”，A2：2字节“终端地址”，A3：1字节“主站地址和组地址标志”
//AFN----------------02
//AFN=======>应用层功能码
// 如终端上线时需要发送“登陆”信息来告知主站，AFN=02H
// 主站接收到终端的登陆信息时，发送“确认”或“否认”报文来告知终端时，AFN=00H
// AFN=00H(确认/否认)
// AFN=01H(复位)
// AFN=02H(链路接口检测)
// AFN=04H(设置参数)
// AFN=0AH(查询参数)
// AFN=0CH(请求1类数据)
// AFN=0DH(请求2类数据)
// AFN=10H(数据转发)
//SEQ----------------------- 71
//SEQ===========>帧序列域
//D7      TpV:   0表示附加信息域无时间标签，反之有。
//D6-D5   FIR~FIN：定义帧为单帧或多帧
//D4 CON：请求确认标志，若为1，对方收到需回应。
//D3-D0   PSEQ_RSEQ

//（数据单元标识）--------------- 00 00 01 00（4个字节）
//===============================>由信息点标识DA+信息类标识DT组成
//(信息点组DA2---信息点元DA1---信息类组DT2---信息类元DT1
//DA1=0、DA2=0时，表示p0
//（数据单元）
//

//AUX-------------------------
//AUX=================附加信息域
//重要的下行报文PW：必须包含消息认证字段
//事件计数器EC：用于重要的上行报文
//时间标签(6字节)：由SEQ.TpV决定其有无


//CS-----------------85
//16H----------------16
console.info(pack("C9 00 02 34 12 00 02 71 00 00 01 00"))

/**
 * 解开数据包
 */
var reg = /68([0-9]{8})68/g
function extract(str){
    var pack = str.replace(/\s+/g,"");
    console.info(pack)
    //初步提取数据包
    var lenMatch = reg.test(pack);
    if(lenMatch)
    {
        //提取出数据包长度
        var len = extractLen(RegExp.$1);
        //再次进行数据包合法性验证
        var datarestr = "68([0-9]{8})68([0-9A-Za-z]{"+(len*2+2)+"})16";
        var datareg = new RegExp(datarestr,"g");
        var dataMatch = datareg.test(pack);
        if(dataMatch)
        {
            var userdata = RegExp.$2+"";
            var C = userdata.substr(0,2);
            var A   = userdata.substr(2,10);
            var AFN = userdata.substr(12,2);
            var SEQ = userdata.substr(14,2);
            var Data = userdata.substring(16,userdata.length-2);
            var CS   = userdata.substr(userdata.length-2,2);
            console.info(userdata);
            console.info("C",C,"A",A,"AFN",AFN,"SEQ",SEQ,"Data",Data,"CS",CS);
            {
                asfd:"asd",asdf:"xcv"
            }
            return
            {
                "C":C,
                "A":A,
                "AFN":AFN,
                "SEQ":SEQ,
                "Data":Data,
                "CS":CS
            }
        }
    }

    console.info(lenMatch,RegExp.$1,RegExp.$2)
};
extract("68 32 00 32 00 68 C9 00 02 34 12 00 02 71 00 00 01 00 85 16")

/**
 * 输出完整的数据格式
 * @param userdata
 * @returns {string}
 */
function pack(userdata)
{
    userdata = userdata.replace(/\s+/g,"");
    var _L = L(userdata);
    var _CS = CS(userdata);
    return "68"+_L+_L+"68"+userdata+_CS+"16";
}
/**
 * 提取出当前数据包的长度
 */
function extractLen(len2){
    //取一半
    var len = len2.substr(0,4);
    //截图高低位
    var low  = len.substr(0,2);
    //转二进制
    var lowbinary   = fixedLen(parseInt(low,16).toString(2),8);
    var highbinary  = parseInt(len.substr(2,2),16).toString(2);
    var allBinaryLen = highbinary + lowbinary.substr(0,6);
    return parseInt(allBinaryLen,2);
}
/**
 * 计算数据长度
 * @param userdata
 * @returns {string}
 * @constructor
 */
function L(userdata){
    var str = userdata.replace(/\s+/g,"");
    var len = str.length/2;
    var binaryStr = len.toString("2");
    //尾部追加10
    var allBinaryStr = binaryStr + "10";
    //转16进制
    var hexStr = Number(parseInt(allBinaryStr,"2")).toString(16);
    //补0
    if(hexStr.length<4)
    {
        var l = 4-hexStr.length;
        for(var i=0;i<l;i++)
        {
            hexStr = "0"+hexStr;
        }
    }
    //高、低位置,调换
    var result = hexStr.substr(hexStr.length-2,2) + hexStr.substr(hexStr.length-4,2);
    return result;
}

/**
 * 生成cs校验码
 * @param str
 */
function CS(str){
    var result = binarySum(str);
    if(result.length>2)
    {
        return result.substr(result.length-2,2)
    }
    else if(result.length == 2)
    {
        return result;
    }
    else
    {
        return "0"+result;
    }
}

/**
 * 将字符串进行拆分，然后求和
 * @param str
 * @returns {*}
 */
function binarySum(str){
    str = str.replace(/\s+/g,"");
    var binaryArray = toBinaryArray(str);
    //先取出第一个，然后依次加后面的值
    var sum = binaryArray[0];
    for(var i=1;i<binaryArray.length;i++)
    {
        sum = addHex(sum,binaryArray[i]);
    }
    return sum;
};
/**
 * 字符串转数组
 * @param str
 * @returns {Array}
 */
function toBinaryArray(str){
    var result = [];
    for(var i=0;i<str.length/2;i++)
    {
        result.push(str.substr(2*i,2));
    }
    return result;
};
/**
 * 二进制求和
 * @param a
 * @param b
 * @returns {string}
 */
function addHex(a, b) {
    var dec = Number(parseInt(a, 16)) + Number(parseInt(b,16));
    return dec.toString(16);
};
/**
 * 将字符串长度固定
 * @param str
 * @param len
 * @returns {*}
 */
function fixedLen(str,len)
{
    //太长则截取
    if(str.length>len)
    {
        return str.substr(0,len);
    }
    var deltaLen = len-str.length;
    for(var i=0;i<deltaLen;i++)
    {
        str = "0" + str;
    }
    return str;
}
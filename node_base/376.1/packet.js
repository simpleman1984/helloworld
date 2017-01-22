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
//C==================控制域
//D7    传输方向位DIR（DIR=0：表示此帧报文是由主站发出的下行报文； DIR=1：表示此帧报文是由终端发出的上行报 文。）
//D6    启动标志位PRM（PRM =1：表示此帧报文来自启动站；PRM =0：表示此帧报文来自从动站。 ）
//D5    帧计数位FCB;要求访问位ACD
//D4    帧计数有效位FCV，保留
//D3-D0  功能码

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
//终端心跳包
//console.info("上行报文(终端发出)：",extract("68 32 00 32 00 68 C9 00 02 34 12 00 02 71 00 00 01 00 85 16"));
// console.info("下行报文(主站发出)：",extract("68 32 00 32 00 68 0B 00 02 34 12 00 00 61 00 00 01 00 B5 16"));
//复位终端
// console.info("下行报文(主站发出)：",extract("68 8A 00 8A 00 68 41 00 02 34 12 04 01 F0 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 28 53 13 07 05 19 16"));
// console.info("上行报文(终端发出)：",extract("68 52 00 52 00 68 A0 00 02 34 12 04 00 E0 00 00 01 00 11 00 00 27 52 13 07 05 76 16"));
//数据抄读
//(发送请求)
//console.info("请求读数:",extract("68 E2 00 E2 00 68 4B 00 02 34 12 04 10 E0 00 00 01 00 1F 6B BC 0A 10 00 68 61 10 01 30 12 15 68 11 04 33 33 34 33 7B 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 13 51 23 21 05 A1 16 "));
//（回复数据）
console.info("答复的读数:",extract("68 AE 00 AE 00 68 A8 00 02 34 12 04 10 E0 00 00 01 00 1F 14 00 68 61 10 01 30 12 15 68 91 08 33 33 34 33 AB 39 33 33 49 16 4F 00 00 06 50 23 21 05 AE 16 "))

/**
 * 解开数据包
 */
function extract(str){
    var reg = /68([0-9A-Za-z]{8})68/g
    var pack = str.replace(/\s+/g,"");
    console.info(pack)
    //初步提取数据包/
    var lenMatch = reg.test(pack);
    var data;
    if(lenMatch)
    {
        //提取出数据包长度
        var len = extractLen(RegExp.$1);
        //再次进行数据包合法性验证
        var datarestr = "68([0-9A-Za-z]{8})68([0-9A-Za-z]{"+(len*2+2)+"})16";
        var datareg = new RegExp(datarestr,"g");
        var dataMatch = datareg.test(pack);
        if(dataMatch)
        {
            var userdata = RegExp.$2+"";
            var C = userdata.substr(0,2);
            var CBinary = parseInt(C,16).toString(2);
            //传输方向位
            //DIR=0：表示此帧报文是由主站发出的下行报文； DIR=1：表示此帧报文是由终端发出的上行报 文。
            var CDir = CBinary.substr(0,1);
            //启动标志位
            //PRM =1：表示此帧报文来自启动站；PRM =0：表示此帧报文来自从动站。
            var CPrm = CBinary.substr(1,1);
            //帧计数有效位 FCV
            //FCV=1：表示 FCB 位有效；FCV=0：表示 FCB 位无效。
            //当帧计数有效位 FCV=1 时，FCB 表示每个站连续的发送/确认或者请求/响应服务的变化位。
            var CFcv = CBinary.substr(3,1);
            //帧计数位 FCB
            //要求访问位 ACD
            //ACD 位用于上行响应报文中。
            //ACD=1 表示终端有重要事件等待访问，则附加信息域中带有事件计数器 EC（EC 见本部分 4.3.4.6.3）；ACD=0 表示终端无事件数据等待访问。
            var CFcbOrAcd = CBinary.substr(2,1);
            //功能码
            //当启动标志位 PRM =1 时
            //功能码	帧类型	服务功能
            // 0	——	备用
            // 1	发送!确认	复位命令
            // 2～3	——	备用
            // 4	发送!无回答	用户数据
            // 5～8	——	备用
            // 9	请求!响应帧	链路测试
            // 10	请求!响应帧	请求 1 级数据
            // 11	请求!响应帧	请求 2 级数据
            // 12～15	——	备用
            //当启动标志位 PRM =0 时
            // 功能码	帧类型	服务功能
            // 0	确认	认可
            // 1～7	——	备用
            // 8	响应帧	用户数据
            // 9	响应帧	否认：无所召唤的数据
            // 10	——	备用
            // 11	响应帧	链路状态
            // 12～15	——	备用
            var CFunction = parseInt(CBinary.substr(4,4),2);

            var A   = userdata.substr(2,10);
            //行政区划码 A1
            var A1  = A.substr(0,4);
            //终端地址 A2(选址范围为 1～65535。A2=0000H 为无效地址，A2=FFFFH 且 A3 的 D0 位为“  1”  时;表示系统广播地址。)
            var A2  = parseInt(A.substr(4,4),16);
            //主站地址和组地址标志 A3
            //A3 的 D0 位为终端组地址标志，D0=0 表示终端地址 A2 为单地址；D0=1 表示终端地址 A2 为组地 址；A3 的 D1～D7 组成 0～127 个主站地址 MSA。
            var A3  = A.substr(8,2);
            var AFN = userdata.substr(12,2);
            var SEQ = userdata.substr(14,2);
            var SEQBinary = parseInt(SEQ,16).toString(2);
            //TpV=0：表示在附加信息域中无时间标签 Tp； TpV=1：表示在附加信息域中带有时间标
            var SEQTpv = SEQBinary.substr(0,1);
            //FIR置“  1” ，报文的第一帧。   FIN：置“  1” ，报文的最后一帧。
            //FIR	FIN	应用说明
            // 0	0	多帧：中间帧
            // 0	1	多帧：结束帧
            // 1	0	多帧：第 1 帧，有后续帧。
            // 1	1	单帧
            var SEQFir = SEQBinary.substr(1,1);
            var SEQFin = SEQBinary.substr(2,1);
            //在所收到的报文中，CON  位置“  1” ，表示需要对该帧报文进行确认；置“  0” ，表示不需要对该帧报文进行确认。
            var SEQCon = SEQBinary.substr(3,1);

            var Data = userdata.substring(16,userdata.length-2);

            //消息认证码字段 PW( 16 字节组成) + 事件计数器 EC(2字节）
            var auxLen = 0 ;
            //有时间戳TP（6字节）
            if(SEQTpv == "1"){
                auxLen = auxLen + 6;
            }
            //主要数据包部分(需要截取aux的长度）
            var mainData = Data.substring(0,Data.length-auxLen*2);
            //数据单元标识定义（信息点标识 DA 和信息类标识 DT）
            //DA2 采用二进制编码方式表示信息点组，DA1 对位表示某一信息点组的 1～8 个信息点，以此共同构成信息点标识 pn（n=1～2040）
            var Da2 = parseInt(mainData.substr(0,2),16);//1字节（信息点组 DA2）
            var Da1 = mainData.substr(2,2);//1字节（信息点元 DA1）
            console.info(Da2,Da1)
            //信息类 DT, 由信息类元 DT1 和信息类组 DT2 两个字节构成。
            var Dt2 = parseInt(mainData.substr(4,2),16);
            var Dt1 = mainData.substr(6,2);
            console.info(Dt2,Dt1)

            //PW 16字节
            //TP 6字节

            //如果等于1，则信息域中带有时间标(6个字节）
            var Tp ;
            //启动帧帧序号计数器 PFC(1字节）
            var TpPFC;
            //启动帧发送时标(4字节）
            var TpTime;
            //允许发送传输延时时间(单位min)(1字节）
            var TpDelayMin;
            if(SEQTpv == "1"){
                Tp = Data.substr(Data.length-12,12);
                TpPFC = parseInt(Tp.substr(0,2),16);
                //秒分时日=>[年，月]日，时，分，秒
                TpTime = "201701" + reversStr(Tp.substr(2,8));
                TpDelayMin = parseInt(Tp.substr(10,2),16);
            }
            console.info("Tp",Tp,"TpPFC",TpPFC,"TpTime",TpTime,"TpDelayMin",TpDelayMin);

            var DataArray = toBinaryArray(mainData,8);

            var CS   = userdata.substr(userdata.length-2,2);
            data = {
                "C":C,
                "CDir":CDir,
                "CPrm":CPrm,
                "CFcv":CFcv,
                "CFcbOrAcd":CFcbOrAcd,
                "CFunction":CFunction,
                "A":A,
                "A1":A1,
                "A2":A2,
                "A3":A3,
                "AFN":AFN,
                "SEQ":SEQ,
                "SEQTpv":SEQTpv,
                "SEQFir":SEQFir,
                "SEQFin":SEQFin,
                "SEQCon":SEQCon,
                "Data":mainData,
                "DataArray":DataArray,
                "CS":CS
            };
        }
    }
    return data;
};

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
function toBinaryArray(str,fixed){
    fixed = fixed || 2;
    var result = [];
    for(var i=0;i<str.length/fixed;i++)
    {
        result.push(str.substr(fixed*i,fixed));
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
/**
 * 将16进制字符串；两两调换位置
 */
function reversStr(str){
    var len = str.length/2;
    var result="";
    for(var i=0;i<len;i++)
    {
        result+=str.substr(2*(len-i)-2,2);
    }
    return result;
}
/**
 * 二进制转BCD码
 */
function binary2BCD(){

}
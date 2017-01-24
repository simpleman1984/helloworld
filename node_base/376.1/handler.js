var utils  = require("./BinaryUtils");
var packet = require("./packet");

/**
 * 数据回复模板（PDF 第16页）
 */
function confirmData(address){
    return packet.packWith("0B",address,"00","61","00 00 01 00");
}

/**
 * 根据不同的AFN，指定不同的data处理方法<br/>
 * @param afn
 * @param data
 */
function handle(C,A,AFN,SEQ,data,Aux)
{
   console.info(AFN,C,A,extractDiDT(data),Aux)
    // console.info(extractDiDT(data));

    //（链路接口检测）
    //登录；pdf第18页;确认见pdf第16页
    //接收 68 32 00 32 00 68 C9 00 02 34 12 00 02 71 00 00 01 00 85 16
    //发送 68 32 00 32 00 68 0B 00 02 34 12 00 00 61 00 00 01 00 B5 16
    //心跳包
    //接收 68 32 00 32 00 68 C9 00 02 34 12 00 02 71 00 00 04 00 88 16
    //发送 68 32 00 32 00 68 0B 00 02 34 12 00 00 61 00 00 01 00 B5 16
    if(AFN == "02"){
        // Fn  名称及说明  pn
        // F1  登录  p0
        // F2  退出登录  p0
        // F3  心跳  p0
        // F4～F248  备用
        if(is("F1",data)){
            console.error("登录  "+"C:"+C.Content+"     A:"+A.Content+"");
            //回复登录确认（或者取消）
            console.error("登录回复 "+ confirmData(A.Content));
        }
        if(is("F4",data)){
            console.error("心跳包  "+"C:"+C.Content+"     A:"+A.Content+"");
            console.error("心跳包回复 "+ confirmData(A.Content));
        }
    }

    //获取仪表列表
    //设置监测仪表

    else if(AFN=="10"){
        //F1 透明转发数据应答
        if(is("F1",data))
        {
            //以下的解析，请参考（表363 透明转发应答数据单元格式，PDF 183页）
            // 数据内容  数据格式  字节数
            // 终端通信端口号  BIN  1
            // 透明转发内容字节数 k  BIN  2
            // 透明转发内容  k
            //主动抄表回复
            //接收 68 AE 00 AE 00 68 A8 00 02 34 12 04 10 E0 00 00 01 00 1F 14 00 68 61 10 01 30 12 15 68 91 08 33 33 34 33 AB 39 33 33 49 16 4F 00 00 06 50 23 21 05 AE 16
            //发送 68 E2 00 E2 00 68 4B 00 02 34 12 04 10 E0 00 00 01 00 1F 6B BC 0A 10 00 68 61 10 01 30 12 15 68 11 04 33 33 34 33 7B 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 13 51 23 21 05 A1 16
            //000001001f14006861100130121568910833333433ab3933334916

            //通电回复
            //接收 68 8E 00 8E 00 68 A8 00 02 34 12 04 10 E7 00 00 01 00 1F 0C 00 68 61 10 01 30 12 15 68 9C 00 35 16 4F 00 07 15 06 00 22 05 2F 16
            //发送 68 12 01 12 01 68 4B 00 02 34 12 04 10 E7 00 00 01 00 1F 6B BC 0A 1C 00 68 61 10 01 30 12 15 68 1C 10 35 6A 39 35 34 33 33 33 4E 33 33 33 33 63 45 CC 2D 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 15 07 00 22 05 B5 16
            //000001001f0c0068611001301215689c003516

            //断电回复
            //发送 68 12 01 12 01 68 4B 00 02 34 12 04 10 E5 00 00 01 00 1F 6B BC 0A 1C 00 68 61 10 01 30 12 15 68 1C 10 35 6A 39 35 34 33 33 33 4D 33 33 33 33 63 45 CC 2C 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 05 34 05 00 22 05 CC 16
            //接收 68 8E 00 8E 00 68 A8 00 02 34 12 04 10 E5 00 00 01 00 1F 0C 00 68 61 10 01 30 12 15 68 9C 00 35 16 4F 00 05 27 04 00 22 05 3B 16
            //000001001f0c0068611001301215689c003516

            //通电状态查询(断开)
            //发送 68 E2 00 E2 00 68 4B 00 02 34 12 04 10 E6 00 00 01 00 1F 6B BC 0A 10 00 68 61 10 01 30 12 15 68 11 04 36 38 33 37 86 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 06 12 06 00 22 05 55 16
            //接收 68 A6 00 A6 00 68 A8 00 02 34 12 04 10 E6 00 00 01 00 1F 12 00 68 61 10 01 30 12 15 68 91 06 36 38 33 37 83 33 BE 16 4F 00 06 04 05 00 22 05 33 16
            //000001001f120068611001301215689106363833378333be16

            //通电状态查询(通电)
            //发送→ 68 E2 00 E2 00 68 4B 00 02 34 12 04 10 E8 00 00 01 00 1F 6B BC 0A 10 00 68 61 10 01 30 12 15 68 11 04 36 38 33 37 86 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 08 56 07 00 22 05 9E 16
            //接收← 68 A6 00 A6 00 68 A8 00 02 34 12 04 10 E8 00 00 01 00 1F 12 00 68 61 10 01 30 12 15 68 91 06 36 38 33 37 33 33 6E 16 4F 00 08 48 06 00 22 05 DC 16
            console.error("透明数据转发  " + data);
            var result = unpack(data);
            console.info(result);
        }
    }
}

/**
 * 解析（DLT645-2007规约实际内容）
 * @param str
 */
function unpack(str)
{
    var data = str.substr(8);
    //控制码
    var C    = data.substr(0,2);
    //长度
    var len  = parseInt(data.substr(4,2) + data.substr(2,2),16);
    //实际的数据内容（DLT645-2007规约实际内容）
    var content = data.substr(6,len*2);

    //地址域(地址域由6个字节构成，每字节 2 位 BCD 码，地址长度可达 12 位十进制数)
    var subAddress = content.substr(2,12);
    //控制码（1字节）
    var subControl = content.substr(16,2);
    var subControlBinary = parseInt(subControl,16).toString(2);
    //D7传送方向 __ 0：主站发出的命令帧  1：从站发出的命令帧
    var CDir = subControlBinary.substr(0,1);
    //D6从站应答标志 0：从站正确应答 1：有后续数据帧
    var CAnswerTag = subControlBinary.substr(1,1);
    //D5后续帧标志   0：无后续数据帧  1：有后续数据帧
    var CNextTag = subControlBinary.substr(2,1);
    //D4	～D0功能码
    // 00000：保留
    // 01000：广播校时
    // 10001：读数据
    // 10010：读后续数据
    // 10011：读通信地址
    // 10100：写数据.
    // 10101：写通信地址
    // 10110：冻结命令
    // 10111：更改通信速率
    // 11000：修改密码
    // 11001：最大需量清零
    // 11010：电表清零
    // 11011：事件清零
    var CFunctionCode = subControlBinary.substr(3,5);

    //数据长度
    var subLen = parseInt(content.substr(18,2),16);
    //具体内容区域
    var subData = content.substr(20,subLen*2);
    //cs校验码
    var cs = content.substr(-4,2);

    console.info("content:" + content + "....."  + "...len....." + len,"subAddress",subAddress,"subControl",subControl,"subLen",subLen,"subData",subData,"cs",cs);
    console.info(CDir,CAnswerTag,CNextTag,CFunctionCode);

    var result = {};
    //读数据（主动抄表回复）
    if(CFunctionCode == "10001"){
        //控制码：C=91H  无后续数据帧；C=B1H  有后续数据帧。----->(从站正常应答)
        if(subControl == "91"){
            var _data = utils.sub33H(subData);
            //数据类型
            var _dataType = utils.reversStr(_data.substr(0,8));
            var _reverstr = utils.reversStr(_data.substr(8.8));
            var _meterData     = parseFloat(_reverstr.substr(0,6)+ "." + _reverstr.substr(6,2));
            console.info("_dataType",_dataType)
            //(当前)正向有功总电能(DLT645 23页)
            if(_dataType == "00010000"){
                result.dataType = "00010000";
                result.address  = utils.reversStr(subAddress);
                result.control  = subControl;
                result.data     = _meterData;
            }
            //通电状态查询(断开)(通电)
            if(_dataType == "04000503"){
                result.dataType = "04000503";
                result.address  = utils.reversStr(subAddress);
                result.control  = subControl;
                result.data     = _data;
            }
        }
    }
    //通电回复、断电回复
    else if(CFunctionCode == "11100"){
        //从站正常应答帧(控制码：C=9CH 数据域长度：L=00H 帧格式： 68H	A0	…	A5	68H	9CH	00	CS	16)
        if(subControl == "9c"){
            result.address  = utils.reversStr(subAddress);
            result.control  = subControl;
            result.msg = "从站正常应答帧";
        }
        //从站异常应答帧(控制码：C=DCH 数据域长度：L=01H 帧格式：68H	A0	…	A5	68H	DCH	01	ERR	CS	16)
        //第10页；DLT645~
        if(subControl == "DC"){
            result.address  = utils.reversStr(subAddress);
            result.control  = subControl;
            result.msg = "从站正常应答帧";
        }
    }
    return result;
}

/**
 * 判断当前数据包是否为指定格式的数据包
 * @param str
 * @param largeStr
 */
function is(str,largeStr)
{
    var didt = extractDiDT(largeStr);
    //判断是否为F
    if(str.indexOf("F") != -1)
    {
        return didt.F == str;
    }
    //判断是否为P
    if(str.indexOf("P")!= -1)
    {
        return didt.P == str;
    }
}

/**
 * 解析当前的数据标识
 * @param str
 */
function extractDiDT(str){
    var result = {};
    var pstr = str.substr(0,4);
    var fstr = str.substr(4,4);
    if(pstr == "0000"){
        result.P = "p0";
    }
    else
    {
        result.P = "p"+(parseInt(pstr.substr(0,2),16)+(parseInt(pstr.substr(2,2),16)-1)*8);
    }
    result.F = "F"+(parseInt(fstr.substr(0,2),16) + parseInt(fstr.substr(2,2),16)*8);
    return result;
}

exports.handle = handle;
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
 * BCD 减33H
 * @constructor
 */
function sub33H(str){
    var len = str.length/2;
    var result = "";
    for(var i=0;i<len;i++)
    {
        var orig    = str.substr(2*i,2);
        var _result = (parseInt(orig,16) - 16*3-3).toString("16");
        if(_result.length == 1)
        {
            _result = "0" + _result;
        }
        result += _result;
    }
    return result;
}
exports.reversStr = reversStr;
exports.sub33H    = sub33H;
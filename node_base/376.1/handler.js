/**
 * Created by xuaihua on 2017/1/24.
 */
/**
 * 根据不同的AFN，指定不同的data处理方法
 * @param afn
 * @param data
 */
function handle(C,A,AFN,SEQ,data,Aux)
{
    //
    if(AFN=="10"){
        //F1 透明转发数据应答
        if(is("F1",mainData))
        {
            //以下的解析，请参考（表363 透明转发应答数据单元格式，PDF 183页）
        }
    }
}

exports.handle = handle;
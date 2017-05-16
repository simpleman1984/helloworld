import com.google.protobuf.Message;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.coprocessor.ColumnInterpreter;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by xuaihua on 2017/5/11.
 */
public class DoubleColumnInterpreter extends ColumnInterpreter<Long, Long, HBaseProtos.EmptyMsg, HBaseProtos.LongMsg, HBaseProtos.LongMsg>
{
    public DoubleColumnInterpreter() {
    }

    public java.lang.Long getValue(byte[] colFamily, byte[] colQualifier, Cell kv) throws IOException {
        return kv != null && kv.getValueLength() == 8? java.lang.Long.valueOf(Bytes.toLong(kv.getValueArray(), kv.getValueOffset())):null;
    }

    public java.lang.Long add(java.lang.Long l1, java.lang.Long l2) {
        return l1 == null ^ l2 == null?(l1 == null?l2:l1):(l1 == null?null: java.lang.Long.valueOf(l1.longValue() + l2.longValue()) + 5l);
    }

    public int compare(java.lang.Long l1, java.lang.Long l2) {
        return l1 == null ^ l2 == null?(l1 == null?-1:1):(l1 == null?0:l1.compareTo(l2));
    }

    public java.lang.Long getMaxValue() {
        return java.lang.Long.valueOf(9223372036854775807L);
    }

    public java.lang.Long increment(java.lang.Long o) {
        return o == null?null: java.lang.Long.valueOf(o.longValue() + 1L);
    }

    public java.lang.Long multiply(java.lang.Long l1, java.lang.Long l2) {
        return l1 != null && l2 != null? java.lang.Long.valueOf(l1.longValue() * l2.longValue()):null;
    }

    public java.lang.Long getMinValue() {
        return java.lang.Long.valueOf(-9223372036854775808L);
    }

    public double divideForAvg(java.lang.Long l1, java.lang.Long l2) {
        return l2 != null && l1 != null?l1.doubleValue() / l2.doubleValue():0.0D / 0.0;
    }

    public java.lang.Long castToReturnType(java.lang.Long o) {
        return o;
    }

    public java.lang.Long castToCellType(java.lang.Long l) {
        return l;
    }

    public HBaseProtos.EmptyMsg getRequestData() {
        return HBaseProtos.EmptyMsg.getDefaultInstance();
    }

    public void initialize(HBaseProtos.EmptyMsg msg) {
    }

    public HBaseProtos.LongMsg getProtoForCellType(java.lang.Long t) {
        HBaseProtos.LongMsg.Builder builder = HBaseProtos.LongMsg.newBuilder();
        return builder.setLongMsg(t.longValue()).build();
    }

    public HBaseProtos.LongMsg getProtoForPromotedType(java.lang.Long s) {
        HBaseProtos.LongMsg.Builder builder = HBaseProtos.LongMsg.newBuilder();
        return builder.setLongMsg(s.longValue()).build();
    }

    public java.lang.Long getPromotedValueFromProto(HBaseProtos.LongMsg r) {
        return java.lang.Long.valueOf(r.getLongMsg());
    }

    public java.lang.Long getCellValueFromProto(HBaseProtos.LongMsg q) {
        return java.lang.Long.valueOf(q.getLongMsg());
    }
}
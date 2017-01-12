var key = '0102030405060708090a0b0c0d0e0f10';
var iv = '0102030405060708090a0b0c0d0e0f10';
var iconv = require('iconv-lite');
var s = "899AF89AB9218CF8AF79C6E56805C3FD6DC01D3F9C4D833CAE1FE7532B09C2A4D6BC423705E2F0C748D7F83FB5A43CF6E3CBA9BE0FA13EFF8D655C272F9671F79885E351B13EE61E4149F22A93A4CB1B027CE87460CBE78DD8D81A7717337F337EA5206C2874E9395B0D4019D11D09B8817AD0BC47BF6236B1C30373DFFD4E4DDAC30A6CBD52A1A30A87159E556EC651389300D4D51D486BC52E4C188A9C8824899DFB186248373C0FA9630BABCD3566014A130E57BB03196DA0ECF56210B99930558FB090AE2F11EA5127684809D795FF191796088CFC917B148CC6C8D9F12661B494A67AB5CAB79F4BA0B88EF27FBDF5C5115C87DB0170D599D087B8BD860B"

Buffer.prototype.toByteArray = function () {
    return Array.prototype.slice.call(this, 0)
}

// var b1 = new Buffer([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16])
// const buf4 = Buffer.from(iv);
// console.info(buf4.toString('hex'));

var xml="<?xml version=\"1.0\" encoding=\"utf-8\" ?>\
<root>\
<common>\
<building_id>1234567020</building_id>\
<gateway_id>123456702150</gateway_id>\
<type>request</type>\
</common>\
<id_validate operation=\"request\" />\
    </root>"

var crypto = require('crypto');

function encrypt(plaintext) {
    var decipher  = crypto.createCipher('aes-256-cbc', iv);
    decipher.setAutoPadding(true);
    var decrypted = decipher.update(plaintext,'utf8', 'hex');
    decrypted += decipher.final('hex');
    return decrypted;
}

function decrypt(plaintext) {
    var decipher  = crypto.createDecipher('aes-256-cbc',key, iv);
    var decrypted = decipher .update(plaintext, 'hex', 'utf8');
    decrypted += decipher.final('utf8');
    return decrypted;
}
var s1 = encrypt(xml);
console.info(s1);
var s2 = decrypt(s1);
console.info(s2);

// var v = Buffer.from(tt, 'hex')

//var buf = iconv.encode(str, 'GBK');

console.info(s)
function decrypt1(plaintext) {
    var k1 = Buffer.from(key, 'hex');
    console.info(key)
    console.info(k1)
    var decipher  = crypto.createDecipheriv('aes-256-cbc',key, k1);
    decipher.setAutoPadding(false)
    var decrypted = decipher.update(plaintext, 'hex');
    var all = Buffer.concat([decrypted,decipher.final()]);

    console.info("=====",iconv.decode(all, 'utf8'))
    console.info("=====",iconv.decode(all, 'gbk'))
    return decrypted;
}
var s3 = decrypt1(s);
console.info(s3);

// var ciphertext = encrypt(new Buffer("aaaaaaaaaaaaaaaaaaa").toString('utf8'));
// console.info(ciphertext)

// var crypto = require('crypto');
// var cipher = crypto.createCipheriv('aes-128-cbc', key, iv);
// cipher.setAutoPadding(false);
// var crypted = cipher.update(data, 'utf8', 'binary');


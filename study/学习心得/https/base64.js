;(function(){
    var BASE64_MAPPING = [//映射表
        'A','B','C','D','E','F','G','H',
        'I','J','K','L','M','N','O','P',
        'Q','R','S','T','U','V','W','X',
        'Y','Z','a','b','c','d','e','f',
        'g','h','i','j','k','l','m','n',
        'o','p','q','r','s','t','u','v',
        'w','x','y','z','0','1','2','3',
        '4','5','6','7','8','9','+','/'
    ];
    var pad = "=";//补白
    var BASE64_ARC_MAPPING={//反向映射表
        'A':0,'B':1,'C':2,'D':3,'E':4,'F':5,'G':6,'H':7,
        'I':8,'J':9,'K':10,'L':11,'M':12,'N':13,'O':14,'P':15,
        'Q':16,'R':17,'S':18,'T':19,'U':20,'V':21,'W':22,'X':23,
        'Y':24,'Z':25,'a':26,'b':27,'c':28,'d':29,'e':30,'f':31,
        'g':32,'h':33,'i':34,'j':35,'k':36,'l':37,'m':38,'n':39,
        'o':40,'p':41,'q':42,'r':43,'s':44,'t':45,'u':46,'v':47,
        'w':48,'x':49,'y':50,'z':51,'0':52,'1':53,'2':54,'3':55,
        '4':56,'5':57,'6':58,'7':59,'8':60,'9':61,'+':62,'/':63
    };
    /**
     * @param code unicode字符对应的数字
     *
     */
    function getBytesOfUnicideChar(code){//获取unicode字符的UTF-8字节数组
        var bytes = [];
        if(code <= 0x7F){//1个字节
            //00000000-0000007F 0xxxxxxx
            bytes.push(code);
        }
        else if(code <= 0x07FF) {//2个字节
            //00000080-000007FF 110xxxxx 10xxxxxx

            //11000000 + ((code & 00000111 11000000)右移动6位)
            bytes.push(192 + (code&1984)>>6);
            //10000000 + ((code & 00000000 00111111)右移动0位)
            bytes.push(128+(code&63));
        }
        else if(code <= 0xFFFF) {
            //00000800-0000FFFF   1110xxxx 10xxxxxx 10xxxxxx
            bytes.push(224+(code>>12));
            bytes.push(128+((code&4032)>>6));
            bytes.push(128+(code&63));
        }
        else if(code <= 0x1FFFFF) {
            //00010000-001FFFFF 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
            bytes.push(240+(code>>18));
            bytes.push(128+((code&245760)>>12));
            bytes.push(128+((code&4032)>>6));
            bytes.push(128+(code&63));
        }
        else if(code <= 0x13FFFFFF) {
            //00200000-03FF FFFF 111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
            bytes.push(248+(code>>24));
            bytes.push(128+((code&16515072)>>18));
            bytes.push(128+((code&245760)>>12));
            bytes.push(128+((code&4032)>>6));
            bytes.push(128+(code&63));
        }
        else if(code <= 0x7FFFFFFF) {
            //00200000-03FFFFFF 1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
            bytes.push(252+(code>>30));
            bytes.push(128+((code&1056964608)>>24));
            bytes.push(128+((code&16515072)>>18));
            bytes.push(128+((code&245760)>>12));
            bytes.push(128+((code&4032)>>6));
            bytes.push(128+(code&63));
        }
        return bytes;
    }

    function base64Encode(s){
        var rs = "";
        var rm = 2;
        var n  = 0;
        for(var i = 0 ; i < s.length ; i ++){

            var code = s.charCodeAt(i);
            var bytes = getBytesOfUnicideChar(code);
            for(x in bytes){
                b = bytes[x];
                if(rm==8){
                    rs += BASE64_MAPPING[n];
                    rm=2;
                    n=0;
                }
                var c = (n << 8-rm) | (b >> rm);
                rs += BASE64_MAPPING[c];
                n = b & ((2<<rm-1)-1);
                rm += 2;
                code = code >> 8;
            }
        }
        rs += BASE64_MAPPING[n << 8-rm];
        if((md = rs.length%4)!=0){
            rs += new Array(5-md).join(pad);
        }
        return rs;
    };

    function bytesToString(bytes){
        var str = "";
        for(var i = 0 ; i < bytes.length ; i ++){
            var b = bytes[i];
            var code = 0;
            if (b < 128) {
                code = b;
            }
            else if(b<224){
                code = ((b & 31) << 6)
                    + (bytes[++i] & 63);
            }
            else if(b<240){
                code = ((b & 15) << 12)
                    + ((bytes[++i] & 63)<<6)
                    + (bytes[++i] & 63);
            }
            else if(b<248){
                code = ((b & 1) << 24)
                    + ((bytes[++i] & 63)<<18)
                    + ((bytes[++i] & 63)<<12)
                    + ((bytes[++i] & 63)<<6)
                    + (bytes[++i] & 63);

            }
            str+=String.fromCharCode(code);
        }
        return str;
    }


    function base64DecodeToBytes(encoded){
        var bl = 0;
        var p = 0;
        var bytes = [];
        for(var i = 0 ; i < encoded.length; i ++){
            if(encoded.charAt(i)=="=") continue;
            var b = BASE64_ARC_MAPPING[encoded.charAt(i)];
            if(bl==0){
                p = b << 2;
                bl = 6;
            }
            else if(bl<8){
                var c = p | (b >> (bl-2));
                bytes.push(c);
                var rm = bl-2;
                p = (b << (8-rm) & 255);
                bl = rm;
            }
        }
        if(bl>0){
            var c = p << (8-bl);
            bytes.push(c);
        }
        return bytes;
    }

    function base64Decode(str){
        return bytesToString(base64DecodeToBytes(str));
    }

    window.Base64={
        encode:base64Encode,
        decode:base64Decode
    };
}(window,undefined));
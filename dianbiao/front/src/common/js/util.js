import * as _ from 'lodash';
//import { AdminAPI } from '../../api';

var SIGN_REGEXP = /([yMdhsm])(\1*)/g;
var DEFAULT_PATTERN = 'yyyy-MM-dd';
function padding(s, len) {
    var len = len - (s + '').length;
    for (var i = 0; i < len; i++) { s = '0' + s; }
    return s;
}
let timer = 0;
const lockEvent = 'mousedown.lockscreen keydown.lockscreen resize.lockscreen mousemove.lockscreen';
/*
function initLockTimer(lockTimeout) {
    if (timer) {
        clearTimeout(timer);
    }
    
    timer = setTimeout(() => {
    	$('.el-dialog__wrapper').css("display","none");
                	$('.v-modal').css("display","none");
        $(window).off('.lockscreen');
        AdminAPI.logout().then(({data}) => {
        	
            sessionStorage.removeItem('user');
            localStorage.removeItem('user');
            appInst.$alert('系统已经锁定，请重新登录', '提示', {
                confirmButtonText: '登录',
                callback: action => {
//              	$('.el-dialog__wrapper').css("display","none");
//              	$('.v-modal').css("display","none");
                    appInst.$router.replace('/login');
                }
            });
        }).catch(() => {
        });
    }, lockTimeout)
}*/

export default {
   /* initLockScreen(minute) {
        // accountLockTime
        const lockTimeout = (parseInt(minute, 10) || 30) * 60 * 1000;
        var lock = _.debounce(initLockTimer, 500);
        initLockTimer(lockTimeout);
        $(window).on(lockEvent, () => {
            lock(lockTimeout);
        });
    },*/
    dateRangePickerIsValid(daterange) {
        return _.isArray(daterange) && daterange[0] != null && daterange[1] != null;
    },
    getQueryStringByName: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        var context = "";
        if (r != null)
            context = r[2];
        reg = null;
        r = null;
        return context == null || context == "" || context == "undefined" ? "" : context;
    },
    formatDate: {
        format: function (date, pattern) {          
            pattern = pattern || DEFAULT_PATTERN;
            return pattern.replace(SIGN_REGEXP, function ($0) {
                switch ($0.charAt(0)) {
                    case 'y': return padding(date.getFullYear(), $0.length);
                    case 'M': return padding(date.getMonth() + 1, $0.length);
                    case 'd': return padding(date.getDate(), $0.length);
                    case 'w': return date.getDay() + 1;
                    case 'h': return padding(date.getHours(), $0.length);
                    case 'm': return padding(date.getMinutes(), $0.length);
                    case 's': return padding(date.getSeconds(), $0.length);
                }
            });
        },
        parse: function (dateString, pattern) {
            var matchs1 = pattern.match(SIGN_REGEXP);
            var matchs2 = dateString.match(/(\d)+/g);
            if (matchs1.length == matchs2.length) {
                var _date = new Date(1970, 0, 1);
                for (var i = 0; i < matchs1.length; i++) {
                    var _int = parseInt(matchs2[i]);
                    var sign = matchs1[i];
                    switch (sign.charAt(0)) {
                        case 'y': _date.setFullYear(_int); break;
                        case 'M': _date.setMonth(_int - 1); break;
                        case 'd': _date.setDate(_int); break;
                        case 'h': _date.setHours(_int); break;
                        case 'm': _date.setMinutes(_int); break;
                        case 's': _date.setSeconds(_int); break;
                    }
                }
                return _date;
            }
            return null;
        }
    }
};

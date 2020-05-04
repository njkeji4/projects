import Utils from './util';

const dateFormat = (v, format = 'hh:mm:ss') => {
    if(isNaN(v)) {
        return '';
    }
    if(v == 0){
        return '';
    }
    var d;
    try {
        d = new Date(v);
    } catch(e) {
        return '';
    }
    if(isNaN(d.getTime())) {
        return '';
    }
    return Utils.formatDate.format(new Date(v), format);
}


const formatRole = (v) => {
    return v === 'ROLE_ADMIN' ? '管理员': '用户';
};

export default {
    dateFormat,
    formatRole,
};
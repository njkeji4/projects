import axios from 'axios';

import * as AdminAPI from './admin';

// 设置拦截器
axios.interceptors.response.use(undefined, function (e) {
    // 如果session失效,重新登录
    // TODO: mute其它toast|message,提示用户重新登录
    try {
        if (e.response.status === 403 || e.response.status === 405) {
            location.hash = '/login';
        }
    } catch (err) {
    }
});

export {
    
    AdminAPI
    
};
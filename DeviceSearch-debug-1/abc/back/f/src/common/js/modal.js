import Vue from 'vue';
import store from '../../store';
/**
 * 创建modal,要求组件中的el-dialog在close时，在组件上emit '[data,]close'事件
 * component的el-dialog要加ref为modal
 * @param {*} component import的Component
 * @param {*} options merge到Component上的数据
 */
export const openModal = (component) => {
    component.store = store;
    const Component = Vue.extend(component);
    
    return (options = {}) => {
        return new Promise((resolve, reject) => {
            let data;
            const modal = new Component({
                el: document.createElement('div'),
                ...options
            });

            document.body.appendChild(modal.$el);

            modal.$refs.modal.$on('close', () => {
                document.body.removeChild(modal.$el);
                resolve(data);
            });

            // FIXME: 这里默认当有data事件时先close响应
            modal.$on('data', (d) => data = d);
        });
    };
};
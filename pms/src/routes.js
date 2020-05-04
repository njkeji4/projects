import Main from './components/Main.vue';
import HoldView from './components/HoldView.vue';
import Login from './components/Login.vue';
import DeviceList from './components/device/DeviceList.vue';
import DeviceCmd from './components/device/DeviceCmd.vue';
import Usermgt from './components/user/UserManagement.vue';
import groupMgt from './components/group/GroupManagement.vue';
import DeviceStat from './components/device/DeviceStat.vue';
import Overview from './components/overview/Overview.vue';
import BaseTabs from './components/basestation/basetabs.vue';

let routes = [  
    {
        path: '/',
        component: Main,
        name: '',
        children:[       
           
            {
                path: 'business',
                component: DeviceList,
                name:'',
                title:'招商管理',
                iconCls: 'el-icon-help',
                children:[
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'房源招租',                       
                    },
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'申请记录',                       
                    },
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'审批管理',                       
                    }
                ]
                
            },
            
            {
                path: 'rental',
                component: BaseTabs,
                name:'',
                title:'客户管理',
                iconCls: 'el-icon-data-analysis',
                children:[
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'个人客户',                       
                    },
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'企业客户',                       
                    },
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'统计分析',                       
                    }
                ]
            },

            {
                path: 'contract',
                component: DeviceStat,
                name:'',
                title:'合同管理',
                iconCls: 'el-icon-s-data',
                children:[
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'合同信息',                       
                    },
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'到期监控',                       
                    }
                ]
            },

            {
                path: 'repaire',
                component: DeviceCmd,
                name:'',
                title:'物业报修',
                iconCls: 'el-icon-video-play',
                children:[
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'工单列表',                       
                    },
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'统计分析',                       
                    }
                ]
            },
            {
                path: '/moveout',
                component: Usermgt,
                title:'物品放行',
                iconCls: 'el-icon-user',
                admin:1,
                children:[
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'申请记录',                       
                    },
                    {
                        path: 'business',
                        component: DeviceList,                       
                        title:'统计分析',                       
                    }
                ]
            }
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: {
            path: '/business'
        }
    }
   
];
    

export default routes;
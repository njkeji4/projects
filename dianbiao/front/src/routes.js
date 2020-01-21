import Main from './components/Main.vue';
import HoldView from './components/HoldView.vue';
import Login from './components/Login.vue';
import DeviceList from './components/device/DeviceList.vue';
import DeviceCmd from './components/device/DeviceCmd.vue';
import Usermgt from './components/user/UserManagement.vue';
import groupMgt from './components/group/GroupManagement.vue';
import DeviceStat from './components/device/DeviceStat.vue';
import Overview from './components/device/Overview.vue';


let routes = [  
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true        
    }, 
    {
        path: '/',
        component: Main,
        name: '',
        children:[
            {
                path: 'overview',
                component: Overview,
                name:'',
                title:'首页',
                iconCls: 'TACmanagement'
            },
            
            {
                path: 'device',
                component: DeviceList,
                name:'',
                title:'设备',
                iconCls: 'devicemanagement'
            },
            
            {
                path: 'stat',
                component: DeviceStat,
                name:'',
                title:'设备统计',
                iconCls: 'performancemanagement'
            },

            {
                path: 'cmd',
                component: DeviceCmd,
                name:'',
                title:'查看命令',
                iconCls: 'versionmanagement'
            },
            {
                path: 'user',
                component: Usermgt,
                name:'',
                title:'用户管理',
                iconCls: 'user',
                admin:1,
            },
            {
                path: 'group',
                component: groupMgt,
                name:'',
                title:'组管理',
                iconCls: 'group',
                admin:1
            },


            {
                path:'*',
                hidden:true,
                redirect:{
                    path: '/overview'
                }
            }
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: {
            path: '/device'
        }
    }
   
];
    

export default routes;
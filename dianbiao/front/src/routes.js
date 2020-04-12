import Main from './components/Main.vue';
import HoldView from './components/HoldView.vue';
import Login from './components/Login.vue';
import DeviceList from './components/device/DeviceList.vue';
import DeviceCmd from './components/device/DeviceCmd.vue';
import Usermgt from './components/user/UserManagement.vue';
import groupMgt from './components/group/GroupManagement.vue';
import DeviceStat from './components/device/DeviceStat.vue';
import Overview from './components/overview/Overview.vue';


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
                title:'全屏',
                iconCls: 'el-icon-full-screen'
            },
            
            {
                path: 'device',
                component: DeviceList,
                name:'',
                title:'设备',
                iconCls: 'el-icon-help'
            },
            
            {
                path: 'stat',
                component: DeviceStat,
                name:'',
                title:'统计',
                iconCls: 'el-icon-s-data'
            },

            {
                path: 'cmd',
                component: DeviceCmd,
                name:'',
                title:'命令',
                iconCls: 'el-icon-video-play'
            },
            {
                path: 'user',
                component: Usermgt,
                name:'',
                title:'用户',
                iconCls: 'el-icon-user',
                admin:1,
            },
            {
                path: 'group',
                component: groupMgt,
                name:'',
                title:'组管理',
                iconCls: 'el-icon-setting',
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
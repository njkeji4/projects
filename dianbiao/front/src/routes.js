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
import RoomList from './components/rooms/Rooms.vue';

let routes = [  
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true        
    }, 
    {
        path: '/overview',
        component: Overview,
        name:'',
        title:'首页',
        iconCls: 'el-icon-full-screen'
    },  
   
    {
        path: '/',
        component: Main,
        name: '',
        children:[       
            {
                path: 'room',
                component: RoomList,               
                title:'机房',
                iconCls: 'el-icon-s-shop'
            },     
           
            {
                path: 'device',
                component: DeviceList,               
                title:'空开',
                iconCls: 'el-icon-help'
            },            
            {
                path: 'base',
                component: BaseTabs,               
                title:'基站',
                iconCls: 'el-icon-data-analysis'
            },           

            {
                path: 'stat',
                component: DeviceStat,               
                title:'统计',
                iconCls: 'el-icon-s-data'
            },

            {
                path: 'cmd',
                component: DeviceCmd,               
                title:'命令',
                iconCls: 'el-icon-video-play'
            },
            {
                path: 'user',
                component: Usermgt,               
                title:'用户',
                iconCls: 'el-icon-user',
                admin:1,
            },
            {
                path: 'group',
                component: groupMgt,                
                title:'设置',
                iconCls: 'el-icon-setting',
                admin:1
            },

            /*{
                path: 'test',
                component: groupMgt,
                name:'',
                title:'测试',
                iconCls: 'el-icon-setting',
                admin:1,
                children:[
                    {
                        path: 'group',
                        component: groupMgt,
                        name:'',
                        title:'中文子菜单',
                        iconCls: 'el-icon-setting',
                        admin:1
                    },
                    {
                        path: 'group',
                        component: groupMgt,
                        name:'',
                        title:'中文子菜单',
                        iconCls: 'el-icon-setting',
                        admin:1
                    },  
                ]
            },*/


            {
                path:'*',
                hidden:true,
                redirect:{
                    path: '/device'
                }
            }
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: {
            path: '/overview'
        }
    }
   
];
    

export default routes;
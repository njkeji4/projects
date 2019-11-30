import Main from './components/Main.vue';
import HoldView from './components/HoldView.vue';
import Login from './components/Login.vue';
import DeviceList from './components/device/DeviceList.vue';
import DeviceCmd from './components/device/DeviceCmd.vue';
import DataList from './components/datamgt/DataList.vue';
import BlackList from './components/blacklist/BlackList.vue';
import Settings from './components/settings/SystemSettings.vue';
import MainStat from './components/statistic/Stat.vue';
import MainStat2 from './components/statistic/Stat2.vue';
import Alarm from './components/alarm/AlarmList.vue';
import Usermgt from './components/user/UserManagement.vue';
import versionmgt from './components/version/VersionManagement.vue';
import groupMgt from './components/group/GroupManagement.vue';
import advMgt from './components/adv/AdvManagement.vue';
import advDownload from './components/adv/AdvDownloadResult.vue';

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
                path: 'device',
                component: DeviceList,
                name:'',
                title:'设备',
                iconCls: 'devicemanagement'
            },
            
            {
                path: 'stat',
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
                    path: '/device'
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
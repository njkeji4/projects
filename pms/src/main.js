import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import VueRouter from 'vue-router'
import App from './App.vue'
import routes from './routes';
import store from './store';

Vue.config.productionTip = false

Vue.use(ElementUI);
Vue.use(VueRouter);

const router = new VueRouter({
  routes
});

/*
router.beforeEach((to, from, next) => {
  if (to.path == '/login') {
    sessionStorage.removeItem('latest_loginInfo');
  }
  let user = JSON.parse(sessionStorage.getItem('latest_loginInfo'));
  if (!user) {
    if(to.path != '/login') {
      next({ path: '/login' })
    } else {
      next();
    }
  } else { // 权限控制

    next();

    //
   // if(!_.isArray(user.accessRights)) { // 兼容旧接口数据
    //  localStorage.removeItem('latest_loginInfo');
    //  return next({ path: '/login'});
    //}
   // if(checkAccess(user.accessRights, to.name)) {
   //   next()
  //  } else {
  //    next(false); // 
  //  }/
  }
})*/


new Vue({
  router,
  store,
  render: h => h(App), 
}).$mount('#app')
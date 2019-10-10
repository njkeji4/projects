import * as types from '../mutation-types'

const defaultLoginInfo = {
  name: '',
  password:  '',
  role:''
}

const state = { ...defaultLoginInfo };

const mutations = {
  [types.LOGIN_UPDARE] (state, userInfo) {    
    Object.assign(state, defaultLoginInfo, userInfo);
  },
  [types.LOGIN_RESET] (state) {    
    Object.assign(state, defaultLoginInfo);
  },
}

const getters = {
  loginInfo: state => state
}

const actions = {

  logout: ({commit}) => { 
    if(sessionStorage)  
      sessionStorage.removeItem('latest_loginInfo');    
    commit(types.LOGIN_RESET);
  },

  update: ({commit}, userInfo) => {    
    sessionStorage.setItem("latest_loginInfo",  JSON.stringify(userInfo));
    
    var ok = userInfo.loginok;
    userInfo.loginok = false;
    localStorage.setItem(userInfo.name, JSON.stringify(userInfo));
    localStorage.setItem("latest_loginInfo", JSON.stringify(userInfo));    
    commit(types.LOGIN_UPDARE, userInfo);
  },  

  loadUserInfo: ({commit}, name) => {
    let user;
    try {
      user = JSON.parse(localStorage.getItem(name));
      if(user.name === undefined){
        return false;
      }
    } catch(e) {
      return false;
    }
    commit(types.LOGIN_UPDARE, user);
    return true;
  },

  loadUserFromSession: ({commit}) => {
    let user;
    try {
      user = JSON.parse(sessionStorage.getItem("latest_loginInfo"));
      if(user.name === undefined){
        return false;
      }
    } catch(e) {
      return false;
    }
    user.loginok = true;
    commit(types.LOGIN_UPDARE, user);
    return true;
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  getters,
  actions,
}

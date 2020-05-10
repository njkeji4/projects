<template>
<div style="background-repeat:no-repeat;
            background-size:100% 100%;
            background-image:url(./bg.jpg);height:100vh !important;">

    
    <div class="login-title">
       <div class="title0" >
        智慧机楼管理系统
       </div>   
    
      <el-form :model="loginForm" :rules="loginFormRule" ref="loginForm" label-position="left" 
          label-width="0px" 
          class="login-container">
      

        <h3 class="title"> 输入用户名，密码进行系统管理	</h3>
        <el-form-item prop="name">
          <el-input type="text" v-model="loginForm.name" auto-complete="off" placeholder="账号" @keyup.native.enter="submit"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码" @keyup.native.enter="submit"></el-input>
        </el-form-item>
        <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>
        <el-form-item style="width:100%;">
          <el-button type="primary" style="width:100%;" @click.native.prevent="submit" :loading="logining">登录</el-button>
          <!--<el-button @click.native.prevent="handleReset2">重置</el-button>-->
        </el-form-item>   
      </el-form>
    </div>

</div>
</template>

<script>
  import {mapGetters, mapActions} from 'vuex';  
  import { AdminAPI } from '../api';
  import md5 from 'blueimp-md5';
  
  //import NProgress from 'nprogress'
  const defaultFormData = {
    name: '',
    password: '',
    role:''
  };
  export default {
    data() {
      return {
       
        logining: false,
        savedInfo: {},
        loginForm: defaultFormData,
        imageData:"data:image/jpg;base64,",
        loginFormRule: {
          account: [
            { required: true, message: '请输入账号', trigger: 'blur' },
          ],
          checkPass: [
            { required: true, message: '请输入密码', trigger: 'blur' },
          ]
        },
        checked: true
      };
    },

    computed: {
			...mapGetters('login', {loginUserInfo : 'loginInfo'}),
		},

    methods: {

      ...mapActions('login', ['logout', 'update', 'loadUserInfo', 'loadUserFromSession']),
		
      submit() {

        var loginParams = {
                name:this.loginForm.name,
                password:md5(this.loginForm.password)
        };

        var _this = this;
        this.$refs.loginForm.validate((valid) => {
            if(valid) {
              
              this.logining = true;
              
              AdminAPI.login(loginParams).then(  ({data}) =>
                  {           
                      if(data.status === 0){      
                        this.loginForm.role = data.data.role;                  
                        this.update(this.loginForm);
                        var firstpage='/overview';                        
                        this.$router.push({path: firstpage });                       
                      }else{            
                        this.$message({
                              message: data.msg,
                              type: 'error'
                            });
                      }
                      this.logining = false;
                  })
                  .catch(ex =>
                  {
                   this.$message({
                              message: "登陆错误",
                              type: 'error'
                            });
                    this.logining = false;
                  });
            }//end if(valid)
          },
        );
		  },//end submit method          

    },

    created() {      
      if(this.loadUserInfo("latest_loginInfo")){
          Object.assign(this.loginForm, this.loginUserInfo);
      }
    }
  }

</script>

<style lang="scss" scoped>
  .login-title{
      position:absolute;
      right:150px;
      top:80px;
      width: 350px;
      .title0{
        font-family:Tahoma,Arial,"Hiragino Sans GB","宋体","微软雅黑";
        color:white;
        font-size:40px; 
        margin-bottom:10px;
      }
  }

  .login-container {        
        /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
       
        border-radius: 5px;     
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;  

        padding: 35px 35px 15px 35px;
        background-color: rgba(0,39,74,0.6);
       
       
        .title {
          margin: 0px auto 40px auto;
          text-align: center;      
          font-family:Tahoma,Arial,"Hiragino Sans GB","宋体","微软雅黑";
          color:white;
        }
        .remember {
          margin: 0px 0px 35px 0px;
          color:white;
        }
  }
</style>

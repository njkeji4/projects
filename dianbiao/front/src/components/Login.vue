<template>
<div style="">

      <!--img height="55px" src="../assets/img/timg.jpg" style="position:fixed;right:540px; top:50px;"/-->
      <div style="font:'微软雅黑'; font-size:40px;position:fixed;right:215px; top:50px; 
              color:#40a1db;vertical-align:top;line-height:55px;vertical-align:middle;">
        人证核验管理系统
     </div>
    
    <div style="float:left;margin-top:0px;width:60%;">    
        <img style="width:100%;" src="../assets/img/login-poto.gif" />
    </div>

    <el-form :model="loginForm" :rules="loginFormRule" ref="loginForm" label-position="left" label-width="0px" class="demo-ruleForm login-container">
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

   
    <!--div style="color:#40a1db;width:100%; position:fixed;bottom:20px;" >
      中移动物联网有限公司 Copyright © 2019							
    </div-->

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
        title:"欢迎登陆使用",//人证管理系统
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

        if(0){//for test
            this.$router.push({
                    path: '/statis'
                  });

                  return;
        }

        var loginParams = {
                name:this.loginForm.name,
                password:md5(this.loginForm.password)
        };

        var _this = this;
        this.$refs.loginForm.validate((valid) => {
            if(valid) {
              
              this.logining = true;
              
              AdminAPI.login(loginParams)
                  .then(({data}) =>
                  {           
                      if(data.status === 0){      
                        this.loginForm.role = data.data.role;                  
                        this.update(this.loginForm);
                        var firstpage='/statis';

                        if(data.data.role==='ROLE_AD')
                        {
                            firstpage='/adv/adv';
                        }
                        this.$router.push({
                          path: firstpage
                        });
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

      test(){
         AdminAPI.getCheckDataById("089fe534-115e-4969-b8ed-97319a193dcb")
                  .then(({data}) =>
                  {           
                      if(data.status === 0){      
                        this.imageData += data.data.spotImg;
                        console.log(this.imageData);
                      }else{            
                        this.$message({
                              message: "get checkdata failed",
                              type: 'error'
                            });
                      }
                      
                  })
                  .catch(ex =>
                  {
                   this.$message({
                              message: "get check data failed"+ex,
                              type: 'error'
                            });
                    
                  });
      }

    },

    created() {      
      if(this.loadUserInfo("latest_loginInfo")){
          Object.assign(this.loginForm, this.loginUserInfo);
      }
    }
  }

</script>

<style lang="scss" scoped>
  .login-container {
   position:absolute;
   right:150px;
   top:150px;
     
    /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
   
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .title {
      margin: 0px auto 40px auto;
      text-align: center;      
      font-family:Tahoma,Arial,"Hiragino Sans GB","宋体","微软雅黑";
      color:#333333;
      
    }
    .remember {
      margin: 0px 0px 35px 0px;
    }
  }
</style>

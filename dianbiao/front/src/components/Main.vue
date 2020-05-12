<template>	
		<el-container style="min-height:100vh;">

			<el-aside width="50px" >				
			<el-container style="height:100%;overflow:hidden;"> <!-- this style is very important-->
					
				<el-header  style="background-color: #3c8dbc;
							       color: #fff;padding:0;
							       font-size:30px; line-height: 50px; height: 50px;">
					<i class="el-icon-eleme"/>
				</el-header>

				<el-menu default-active="100" :collapse="true" style="height:100%;"> 
					<!--el-scrollbar style="height:100%"-->
					
						<el-menu-item tabindex="100" @click="nav('overview')">
							<i class="menu-icon-1 el-icon-full-screen"> 
								<div slot="title" class="menu-text">首页</div>
							</i>
						</el-menu-item>
						
						<template v-for="(item,index) in sideMenuRouter" 
							v-if="sysUserInfo.role === 'ROLE_ADMIN' 
									|| (sysUserInfo.role === 'ROLE_USER' && !item.admin)">									
							
							<el-submenu :index="index+''" v-if="item.children" 
										popper-class="sub-menu-popup"
										tabindex="-1">
								<template slot="title">
									<i :class="item.iconCls" class="menu-icon-1">
										<div class="menu-text">{{item.title}}</div>
									</i>
								</template>
								<el-menu-item v-for="(child, i) in item.children" :index="i+''" v-if="!child.hidden" @click="nav(item.path + '/' + child.path)">
									<span>{{child.title}}</span>
								</el-menu-item>
							</el-submenu>

							<el-menu-item v-else :tabindex="index+''" @click.native="nav(item.path)" >
								<i :class="item.iconCls" class="menu-icon-1">
									<div class="menu-text">{{item.title}}</div>
								</i>
							</el-menu-item>

						</template>

					<!--/el-scrollbar-->
				</el-menu>
				
			</el-container>				
			</el-aside>	<!-- 左侧面 -->
			
			<el-container style="height:100%;">

				<el-header style="background:rgba(2,9,17,1);color: #fff;
								 line-height: 50px;height: 50px;">
					
					<div style="color: #fff;
							font-family: Helvetica Neue,Helvetica,Arial,sans-serif;
							font-weight: 400; line-height: 50px; height: 50px;float:left;">					 
						智慧机房管理系统
					</div>

					<div style="float:right">
						<!--el-dropdown trigger="click" @command="handleCommand">
							<span class="el-dropdown-link" style="color:white;">
								<i  class="el-icon-user-solid" style="font-size:25px;font-weight:bold;"></i>
								{{sysUserInfo.name}}
								<i class="el-icon-arrow-down el-icon--right"></i>
							</span>
							<el-dropdown-menu slot="dropdown" >							
								<el-dropdown-item  command="changePwd">修改密码</el-dropdown-item>
								<el-dropdown-item  command="logout">退出登录</el-dropdown-item>
							</el-dropdown-menu>
						</el-dropdown-->
						
						{{currenttime}} <span style="color:grey">|</span> {{currentdate}}

					</div>

				</el-header>

				<el-main id="elmain" style="background-color: #ecf0f5; 
								margin:10px;padding:0px;">
					
					<transition name="fade" mode="out-in">
						<router-view/>
					</transition>

				</el-main>

			

			</el-container>

		</el-container>

</template>

<script>

	import axios from 'axios';
	import Vue from 'vue'
	import { mapGetters, mapActions } from 'vuex';
	import util from '../common/js/util';
	import { AdminAPI } from '../api';
	import { openModal } from '../common/js/modal';
	import SockJs from 'sockjs-client';
	import Stomp from 'stompjs';

	import ChangePassword from './changePassword';
	const openChangePasswordModal = openModal(ChangePassword);

	export default {
		data() {
			return {	
				isCollapse: true,
				sysUserName: '',
				currenttime:'11',
				currentdate:'22',
			}
		},
		computed: {

			sideMenuRouter() {
				var children = this.$router.options.routes[2].children;				
				return children;
			},
			...mapGetters('login', {
				sysUserInfo: 'loginInfo'
			}),
			
		},
		filters: {
			//formatRole
		},
		methods: {			
			 ...mapActions('login', ['logout', 'loadUserFromSession']),
			 collapse(){
				this.isCollapse = !this.isCollapse;
			},
			nav(path) {	
				this.$router.push('/' + path);
			},
			
			//退出登录
			handleLogout() {
				var _this = this;				
				this.$confirm('确认退出吗?', '提示', {
					//type: 'warning'
				}).then(() => {
					AdminAPI.logout().then(({
						data
					}) => {
						_this.logout();
						_this.$router.replace('/login');
					}).catch(() => {});
				}).catch(() => {});
			},
			changePassword(){
				openChangePasswordModal({
					data: {
						username: this.sysUserInfo.name
					}
				}).then((data) => {
					if(data) {
						this.$message({
							type: 'success',
							message: '密码修改成功!'
						});
					}
				}).catch(() => {
					this.$message.error('密码修改异常!');
				});
			},		
			
			handleCommand(command) {				
				if(command === 'logout') {
					this.handleLogout();
				}
				if(command === 'changePwd'){
					this.changePassword();
				}
			},
			connectToWebsocket(){
				var socket = new SockJs(AdminAPI.websocketurl);
				var stompClient = Stomp.over(socket);
				stompClient.connect({}, () => {
					//stompClient.send("/app/hello", {}, 'hello');
					stompClient.subscribe('/topic/greetings/'+this.sysUserInfo.name,(msg) => {						
						this.openAlarm(msg);
					});
				}, (err) => {
					console.log("err:" + err);
				});
			},
			openAlarm(msg){
				console.log("msg:" +  msg.body);
				//this.$alert("操作结果通知",msg,{confirmButtonText:'确定'});
				this.$message(msg.body);
				//document.getElementById("alarmAudio").play();	

				//console.log("get alarm==========");
				this.$router.push({
					path: '/device?'+Date.now()
				});		
			},

		},
		created() {
			this.connectToWebsocket();
		},
		mounted() {
			if(!this.loadUserFromSession()) {
				this.$router.replace('/login');
			}
			 const _this = this;
			setInterval(function(){
				var myDate=new Date();
				var m = myDate.getMinutes();
				var s = myDate.getSeconds();
				m = m < 10 ? '0' + m : m;
				s = s < 10 ? '0' + s : s;
				_this.currentdate = myDate.getFullYear() + '年'+ (myDate.getMonth() + 1) + '月' + myDate.getDate();
				_this.currenttime = myDate.getHours()+':'+ m  + ":" + s;				
			},1000);			
			}
	}
</script>

<style lang="scss">
	
	.el-main{
		height:100%;
		overflow:hidden;
	}

	.el-dropdown-link {
		cursor: pointer;		
	}
	.el-icon-arrow-down {
		font-size: 12px;
	}
	.pointer{
		cursor: pointer;
		font-size:30px;
		line-height:50px;
		height:50px;
	}
	
	.userinfo {
		margin-left: -20px;
		width: 25px;
		height: 25px;
		display: inline-block;
		
	}
	

	/* ul */
	.el-menu--collapse{
		width:50px !important;
		height:100%;
		background-color:rgb(47,71,133);
	}
	/* li */
	.el-menu-item, .el-menu-item.is-active, .el-submenu{
		padding-left:0px !important;
		text-align:center;
		width:50px !important;
	}
	.el-menu-item:hover{		
		background-color: transparent;
	}
	.el-menu-item:focus,.el-submenu:focus{
		background-color: rgb(30, 40, 44);
		color:white;	
	}
	
	.el-submenu__title{
		padding-left:0px !important;
	}
	.el-submenu__title:hover{
		background-color: transparent;
	}
	.sub-menu-popup{
		background-color:rgb(28,34,75);
		ul{
			background-color:rgb(28,34,75);
			.el-menu-item{
				width:100% !important;
				color:white;
			}
			.el-menu-item:hover{
				color:blue;
				background-color:rgb(34,50,104);
			}
		}
	}

	/* icon */
	.menu-icon-1{
		font-size:30px;
		width:50px !important;
		text-align:center;
		color:white !important;
	}
	.menu-icon-1:hover{
		color:blue !important;
	}	
	.menu-text{
		font-size:15px;
		color: #8aa4af;
	}
	
	
</style>
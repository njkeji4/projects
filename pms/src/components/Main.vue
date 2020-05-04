<template>	
		<el-container style="" class="pagecontainer">
			<el-header class="pageheader">
				<div class="logo"></div>
			</el-header>

			<el-container class="pagemain">

				<el-aside class="pageside">
					<el-menu default-active="100" :collapse="isCollapse">
						
						<template v-for="(item,index) in sideMenuRouter">
							
							<el-submenu :index="index+''" v-if="item.children" popper-class="sub-menu-popup" tabindex=-1>
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
					</el-menu>
				</el-aside>
				

				<el-main id="elmain" style="background-color: transparent; 
							box-sizing: border-box; padding-left:10px;padding-top:0px;
							overflow-x:hidden;">
				
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
				var children = this.$router.options.routes[0].children;	
						
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
				/*
				if(path === 'overview'){
					var ele= document.getElementById("elmain");
					var requestMethod = ele.requestFullScreen  ||ele.webkitRequestFullScreen //谷歌
					||ele.mozRequestFullScreen  //火狐
					||ele.msRequestFullScreen; //IE11;
							requestMethod.call(ele); 
				}*/
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
			
		},
		mounted() {
		
		}
	}
</script>

<style lang="scss">
	.pagecontainer{
		overflow:hidden;
		height:100vh;
		background-image:url(../assets/bg.png);
		
		.pageheader{
			background-image:url(../assets/headbg.png);
			height:240px !important;
			.logo{
				margin-top:20px;
				background-image:url(../assets/logo.png);
				width:306px;
				height:38px
			}
		}
		.pagemain{		
			height:100%;
			margin-top:-140px;
			.pageside{
				width:90px !important;
			}
		}
	}	
	

	/* ul */
	.el-menu--collapse{
		width:100% !important;
		height:100%;
		background:rgba(10,13,51,0.7);
	}
	/* li */
	.el-menu-item, .el-menu-item.is-active, .el-submenu{
		padding-left:0px !important;
		text-align:center;
		width:100% !important;
	}
	.el-submenu{
		height:85px;
		
	}
	.el-submenu:focus{
		background:linear-gradient(270deg,rgba(23,0,34,0) 0%,rgba(91,110,249,0.3) 88%,rgba(62,244,255,1) 100%) !important;
		color:white;
	}	
	
	.el-menu-item:hover{		
		background-color: transparent;
	}
	.el-menu-item:focus{
		background:linear-gradient(270deg,rgba(23,0,34,0) 0%,rgba(91,110,249,0.3) 88%,rgba(62,244,255,1) 100%);
		color:white;	
	}
	.el-submenu__title{
		padding-left:0px !important;
		width:100% !important;
		vertical-align:text-top;
	}
	.el-submenu__title:hover{
		background-color: transparent;
	}

	/* icon */
	.menu-icon-1{
		font-size:30px;
		width:90px;
		color:white !important;		
	}
	.menu-icon-1:hover{
		color:blue !important;
	}		
	.menu-text{
		font-size:15px;
		color: #8aa4af;
		margin-top:5px;
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
	
</style>
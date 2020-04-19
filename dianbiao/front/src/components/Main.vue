<template>	
		<el-container style="height:100%;min-height:100vh;">

			<el-aside width="50px">
			<el-container style="height:100%;"> <!-- this style is very important-->
					
				<el-header  style="background-color: #3c8dbc;
							       color: #fff;padding:0;
							       font-size:30px; line-height: 50px; height: 50px;">
					
					<i class="el-icon-eleme"/>

				</el-header>

				<el-main class="side-menu">
					<div style="background-color: transparent;  height:100%;">

						<template v-for="(item,index) in sideMenuRouter" 
							v-if="sysUserInfo.role === 'ROLE_ADMIN' 
									|| (sysUserInfo.role === 'ROLE_USER' && !item.admin)">									
							
							<div :index="index+''" v-if="item.children">
								<i :class="item.iconCls"/>
								<div slot="title">{{item.title}}</div>
								<div v-for="(child, i) in item.children" :index="i+''" v-if="!child.hidden" @click="nav(item.path + '/' + child.path)">
									<i :class="child.iconCls"></i><span>{{child.title}}</span>
								</div>
							</div>

							<div v-else  tabindex="0" :index="index+''" @click="nav(item.path)" class="menu-item">
								<i :class="item.iconCls" class="menu-icon"></i>
								<div class="menu-text">{{item.title }}</div>
							</div>

						</template>
					</div>
				</el-main>
				
			</el-container>
			</el-aside>
			
			<el-container>

				<el-header style="background:rgba(2,9,17,1);color: #fff;
								 line-height: 50px;height: 50px;">
					
					<div style="color: #fff;
							font-family: Helvetica Neue,Helvetica,Arial,sans-serif;
							font-weight: 400; line-height: 50px; height: 50px;float:left;">					 
						智能空开管理系统
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
								box-sizing: border-box;
								margin:0px;padding:0px;overflow-x:hidden;">
					
					<transition name="fade" mode="out-in">
						<router-view/>
					</transition>

				</el-main>

				<!--el-footer style="line-height: 40px; height: 40px; 
							border-top:1px solid #d2d6de; text-align: left;">
						Copyright © 2014-2019 中移动物联网有限公司  All rights reserved. 
				</el-footer-->

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
				var children = this.$router.options.routes[1].children;				
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
			this.connectToWebsocket();
		},
		mounted() {
			if(!this.loadUserFromSession()) {
				this.$router.replace('/login');
			}
			 const _this = this;
			setInterval(function(){
				var myDate=new Date();
				
				_this.currentdate = myDate.getFullYear() + '年'+ (myDate.getMonth() + 1) + '月' + myDate.getDate();
				_this.currenttime = myDate.getHours()+':'+myDate.getMinutes()+":"+myDate.getSeconds();				
			},1000);			
			}
	}
</script>

<style lang="scss">
	.el-aside{
		overflow:hidden;
	}
	.el-main{
		height:100%;
		overflow:hidden;
	}

	/******** start to define side menu CSS */
	.side-menu{
		background-color: rgb(47,71,133);
		padding-left:0px;
		padding-right:0px;
		
	}
	
	/* menu item   */
	.el-menu-item , .el-menu-item.is-active{  /* to fix the issue of elementui*/
		color: #8aa4af;
		text-align:left;
		padding-left:15px !important;
	}
	.el-menu-item:hover{
		color:white;
		background-color: rgb(30, 40, 44);
	}
	.el-menu-item:focus{
		background-color: rgb(30, 40, 44);
		color:white;	
	}

	/* sub menu   */
	.el-submenu{
		text-align:left;
		
		.el-menu--inline{
			margin-left:20px;
		}
	}
	.el-submenu__title{
		color:#8aa4af;
		padding-left:15px !important;
	}
	.el-submenu__title:hover{
		color:white;
		background-color: rgb(30, 40, 44);
	}
	.el-submenu__title:focus{
		color:white;
		background-color: rgb(30, 40, 44);
	}
	.el-menu{
		background-color:transparent;
	}

	.el-menu--collapse{
		width:50px !important;
	}
	.el-menu--popup{
		background-color: #222d32 !important;
		margin-left:0px !important;
	}
	
	/******* end */

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
	
	.menu-item {
		color:white;
		height:50px;
		font-size:29px;
		margin-bottom:10px;
	}
	.menu-item:hover{
		color:blue;
	}
	.menu-item:focus{
		background-color:rgb(18,43,60);
	}
	.menu-text{
		font-size:10px;
	}
	
</style>
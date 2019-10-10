<template>
	<div class="main-content" style="padding:3px;">
		<el-row :gutter=20 class="toolbar searchparam">
			<el-col :span=50 class="paramleft" style="float:left;">
				<el-form :inline="true" size="small" :model="searchForm" class="search-form" label-width="6em" ref="searchForm">
					<el-form-item label="" prop="deviceName">
						<el-input size="small" v-model="searchForm.deviceName" placeholder="设备名称"></el-input>
					</el-form-item>
					
					<el-form-item label="" prop="result">
						<el-select clearable size="small" v-model="searchForm.result" placeholder="结果">
							<el-option  label="通过" value="1"></el-option>
							<el-option  label="不通过" value="2"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="" prop="name">						
						<el-input size="small" v-model="searchForm.name" placeholder="姓名"></el-input>						
					</el-form-item>	
					<el-form-item label="" prop="cardNo">						
						<el-input size="small" v-model="searchForm.cardNo" placeholder="身份证"></el-input>						
					</el-form-item>				

					<el-form-item class="date-range" label="" prop="dateTimeRange" label-width="6.5em">
							<el-date-picker size="small" v-model="searchForm.dateTimeRange" 
								type="datetimerange"
								range-separator="至"
								start-placeholder="比对开始日期"
								end-placeholder="比对结束日期">
							</el-date-picker>
					</el-form-item>	
					<el-button size="small" @click="search">查询</el-button>	
					<el-button size="small" @click="reset">清除</el-button>	

					<el-button size="small" @click="exportList" style="margin-right:10px;">导出</el-button>					
					
				</el-form>
			</el-col>
		</el-row>

		<section class="grid-content">
			<el-table :data="checkresult" resizable border highlight-current-row stripe v-loading="listLoading" ref="table"  
				@selection-change="handleSelectionChange" @row-contextmenu="handleContextMenu"  
				@sort-change="handleSortChange"
				@row-dblclick="handleDblClickRow" class="cmcc-cell-nowrap">

				<el-table-column header-align="center"  type="selection">
				</el-table-column>
				
				<el-table-column sortable="custom"  prop="deviceName" label="设备名字" width="150"></el-table-column>
				<！--el-table-column sortable="custom"  prop="deviceNo" label="设备编号" width="150"></el-table-column-->						
				<el-table-column sortable="custom" prop="result" label="结果" >
					<template slot-scope="scope">						
						<el-tag :type="scope.row.result == '1' ? 'success' : 'danger'" close-transition>{{scope.row.result == '1'?'通过':'不通过'}}</el-tag>					
					</template>
				</el-table-column>				
				
				<el-table-column sortable="custom"  prop="name" label="姓名" width="150"></el-table-column>	
				<el-table-column sortable="custom" prop="cardNo" label="身份证号码" width="180"></el-table-column>	
				<!--el-table-column  prop="score" label="分数">
					<template slot-scope="scope">
						{{scope.row.score | scoreFormat}}
					</template>
				</el-table-column-->				
				<!--el-table-column  prop="threshold" label="阀值" ></el-table-column-->
				<el-table-column sortable prop="compareDate" label="比对日期" width="160">
					<template slot-scope="scope">
						{{scope.row.compareDate | dateFormat}}
					</template>
				</el-table-column>	

				<el-table-column  prop="cardImg" label="身份证相片" width="100">
					<template slot-scope="scope">
						<img :src="'data:image/jpg;base64,'+scope.row.cardImg" width="100" height="100" />
					</template>
				</el-table-column>

				<el-table-column  prop="spotImg" label="现场相片" width="100" >
					<template slot-scope="scope">
						<img :src="'data:image/jpg;base64,'+scope.row.spotImg" width="100" height="100" @click="getSpotImg(scope.row.id)"/>
					</template>
				</el-table-column>

				<el-table-column   prop="folk" label="民族" ></el-table-column>						
				<el-table-column  prop="sex" label="性别" >
					<template slot-scope="scope">						
						{{scope.row.sex == '1'?'男':'女'}}					
					</template>
				</el-table-column>				
				<!--el-table-column  prop="birthday" label="生日" width="160"></el-table-column-->	
				<el-table-column  prop="address" label="地址" width="160"></el-table-column>				
				<el-table-column  prop="validdate1" label="有效期起始日期" width="160"></el-table-column>	
				<el-table-column  prop="validdate2" label="有效期终止日期" width="160"></el-table-column>
				<el-table-column  prop="authority" label="发证机关" ></el-table-column>	
			</el-table>
		</section>
		<el-col class="toolbar">			
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page" 
				:page-sizes="[10, 15, 20]" :page-size="pageSize" 
				layout="total, sizes, prev, pager, next, jumper" :total="total">
			</el-pagination>
		</el-col>		
	</div>
</template>

<script>
	import axios from 'axios';
	import Vue from 'vue'
	import { mapGetters, mapActions } from 'vuex';

	import util from '../../common/js/util';
	import { AdminAPI } from '../../api';
	import Filters from '../../common/js/filters';

	import { openModal } from '../../common/js/modal';

	import SpotImgDlg from './SpotImgDlg';
	const openSpotImgDlg = openModal(SpotImgDlg);		

	export default {
		data() {
			return {				
				searchForm: { 
					deviceName:'',					
					result:'',
					name:'',
					cardNo:'',
					dateTimeRange:''
				},
				checkresult: [],
				total: 0,
				page: 1,				
				pageSize: 10,				
				listLoading: false,
				sels: [], //列表选中列
				contextMenuRow: null,
				contextMenuPosition: {
					top: 0,
					left: 0
				},
				sort:'compareDate',
				order:'desc'
			}
		},
		computed: {

		},
		filters: {
			dateFormat: Filters.dateFormat,
			scoreFormat:(v,f) => {return parseFloat(v).toFixed(2);}
		},
		watch: {
			
		},
		methods: {		
			exportList(){
				//AdminAPI.exportBlackList();
				var searchParams = _.omitBy(this.searchForm, (item) => item == "" || _.isNil(item));
				var pama = "?";
				if(searchParams.deviceName !== undefined)pama += "deviceName="+searchParams.deviceName + "&";
				if(searchParams.result !== undefined)pama += "result="+searchParams.result + "&";
				if(searchParams.name !== undefined)pama += "name="+searchParams.name + "&";
				if(searchParams.cardNo !== undefined)pama += "cardNo="+searchParams.cardNo ;
				if(searchParams.dateTimeRange !== undefined && searchParams.dateTimeRange !== null){
					pama +="betrween1=" +  Date.parse(searchParams.dateTimeRange[0]) + "&";
					pama +="betrween2=" + Date.parse(searchParams.dateTimeRange[1]);
				}
				setTimeout(() => {
								window.open(location.origin+"/checkdata/export" + pama);
							}, 0);
			},
			handleSortChange(col){		
				if(col.prop == null)
				{
					return;
				}		
				this.order = (col.order === 'ascending')? 'asc':'desc';
				this.sort = col.prop ;				
				this.search();
			},

			getSpotImg(id){
				console.log(id);
				openSpotImgDlg({
					data: {
						id
					}
				}).then((data) => {
					
				});
			},
						
			reset() {
				this.$refs.searchForm.resetFields();
			},
			
			search() {
				this.page = 1;
				this.getCheckData();
			},
			
			refresh() {
				this.page = 1;
				this.getCheckData();
			},			
			
			handleSizeChange(size) {
				this.pageSize = size;
				this.handleCurrentChange(1);
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getCheckData();
			},
			getCheckData() {
				const searchParams = _.omitBy(this.searchForm, (item) => item == "" || _.isNil(item));
				
				searchParams.page = this.page - 1;
				searchParams.size = this.pageSize;
				searchParams.sort=this.sort;//"compareDate";
				searchParams.order=this.order;//"desc";
				
				if(searchParams.dateTimeRange !== undefined && searchParams.dateTimeRange !== null){
					searchParams.betrween1 = Date.parse(searchParams.dateTimeRange[0]);
					searchParams.betrween2 = Date.parse(searchParams.dateTimeRange[1]);
				}

				this.listLoading = true;
				AdminAPI.searchCheckData(searchParams).then(({
					data
				}) => {
					if(data.status === 0) {
						this.total = data.data.totalElements;
						this.checkresult = data.data.content;
						this.listLoading = false;
					} else {
						this.$message({
							messsage: `获取比对数据失败:${data.message}`,
							type: 'error'
						})
					}
				});
			},
		
			// context menu
			handleSelectionChange: function(sels) {
				this.sels = sels;
				this.ids=_.map(this.sels , (device) => device.id);				
				//console.log(this.ids);
			},
			handleContextMenu(row, event) {
				
			},
			handleDblClickRow(row, event) {				
			},
			
			getSelection() {
				let list;
				if(this.sels && this.sels.length > 0) {
					list = this.sels;
				} else {
					list = this.contextMenuRow ? [this.contextMenuRow] : [];
				}
				return list;
			},
			
			_getSelectedId() {
				let id;
				if(!this.sels || this.sels.length === 0) { // contenxtMenu
					id = [this.contextMenuRow.id];
				} else {
					id = this.sels.map(item => item.id);
				}
				return id;
			},
						
			deviceAlarmView(deviceInfo){
				//console.log(deviceInfo.sn);
				this.$router.push({
					path: '/alarm/'+deviceInfo.sn
				});
			},
			gotoperformance(){
				let id;
				id = [this.contextMenuRow.id];		
				this.$router.push({
					path: '/performance/'+id
				});		
				
			},
			deviceDistribute(deviceInfo) {
				this.$router.push({
					name: 'map',
					params: {
						sn: deviceInfo.sn
					}
				})
			},
			// table column formatter
			deviceTypeFormatter(row) {
				return `${row.manufacturer}-${row.productClass}`;
			},
		},
		created() {
			
		},
		mounted() {
			this.checkresult = this.getCheckData();
		}
	}
</script>

<style scoped lang="scss">

</style>

<style lang="scss">
	.el-table th,
	.el-table__fixed-header-wrapper thead div,
	,
	.el-table__header-wrapper thead div {
		background-color: #177fd8;
		color: #fff;
	}
	
	.el-table .sort-caret.ascending {
		border-bottom: 5px solid #fff;
	}
	
	.el-table .sort-caret.descending {
		border-top: 5px solid #fff;
		border-top-color: #fff !important;
	}
	
	.searchparam {
		.el-button {
			border-radius: 6px;
		}
		@media only screen and (min-width: 1470px) {
			.paramleft {
				width: 75%;
			}
			
		}
		@media only screen and (max-width: 1470px) {
			.paramleft {
				width: 83%;
			}
			.search-action-wrap {
				width: 100%;
			}
		}
	}
	
	.add-device-form {
		.el-input,
		.el-select {
			width: 200px;
		}
		.el-dialog {
			width: 480px;
		}
		.el-form-item__tips {
			font-size: 12px;
			line-height: 1;
			padding-top: 4px;
			position: absolute;
			left: auto;
			right: 0;
			top: 10px;
		}
	}
	
	.device-params-picker {
		.el-input {
			width: 350px;
		}
		.el-dialog {
			width: 650px;
		}
		.el-form-item__error {
			top: 8px;
			left: 360px;
		}
	}
	
	.el-table {
		.el-button {
			background-color: #fff;
			color: #333;
		}
	}
</style>
<template>
	<!-- batch config -->
	<el-dialog :title="title" ref="modal"  width="60%" :visible.sync="modalVisible" :close-on-click-modal="false"
         class="edit-device-form">
         
		<el-form :model="batchEditForm" label-width="7em" :rules="batchEditFormRules" ref="batchEditForm">
            <el-row :gutter=20 class="toolbar searchparam">
                
                <el-col  :span=24 class="search-action-wrap" style="margin-bottom:10px;">
                    
							拉闸时间：<el-time-picker  v-model="offTime" placeholder="拉闸时间">
                            </el-time-picker >

                            合闸时间：<el-time-picker  v-model="onTime" placeholder="合闸时间">
                            </el-time-picker >                    
                    
                    <el-button style="margin-left:10px;" size="small" type="primary" @click="addOffOnTime" :loading="batchConfigLoading">增加</el-button>
                    <el-button style="margin-left:10px;" size="small" type="primary" @click="delOffOnTime" 
                        :disabled="this.sels.length == 0">删除</el-button>
                </el-col>

                 

            </el-row>	

           

		</el-form>
	
        <section class="grid-content">
			<el-table :data="devices" resizable border highlight-current-row stripe v-loading="listLoading" ref="table" 
			  class="cmcc-cell-nowrap"  @selection-change="handleSelectionChange" >

				<el-table-column header-align="center"  type="selection">				
				</el-table-column>

                	<el-table-column header-align="center"  type="index">				
				</el-table-column>
				
				<el-table-column  sortable="custom" prop="deviceName" label="设备名称" width="240"></el-table-column>
			
				<el-table-column  sortable="custom" prop="offTime" label="拉闸时间" width="240">
                    <template slot-scope="scope">
						{{scope.row.offTime | dateFormat}}
					</template>
                </el-table-column>

                <el-table-column  sortable="custom" prop="onTime" label="合闸时间" width="240">
                    <template slot-scope="scope">
						{{scope.row.onTime | dateFormat}}
					</template>
                </el-table-column>

			</el-table>
		</section>


         <div slot="footer" class="dialog-footer">
			    <el-button size="small" @click="modalVisible = false">取消</el-button>
			    <el-button size="small" type="primary" @click="batchEdidtSubmit" :loading="batchConfigLoading">提交</el-button>
		</div>

	</el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';
import { AdminAPI } from '../../api';
import Filters from '../../common/js/filters';

export default {
    data: function() {
        var nameCheck = (rule, value, callback) => { 
                        if(value === this.deviceInfo.name){                            
                            callback(new Error("输入不同的设备名称"));
                        }else{
                            callback();
                        }
                    };
        return {          
           modalVisible: true,			
           batchConfigLoading: false,           
           title: '',
           devices:[],           
           offTime:'',
           onTime:'',
           sels: [], //列表选中列
           listLoading:false,
           batchEditForm:{
               name:'',
              offTime:'',
                onTime:'',
           },
           batchEditFormRules: {
                offTime: [
                    { required: true, message: '选择拉闸时间', trigger: 'change' },
                    //{validator : nameCheck, trigger: 'blur'}
                ],
                 onTime: [
                    { required: true, message: '选择合闸时间', trigger: 'change' },
                    //{validator : nameCheck, trigger: 'blur'}
                ],
           },
        };
    },
	computed: {
		
	},
    created() {                
        this.title = `设备编号: ${this.deviceInfo[0].deviceNo} 等`; 
    },

    filters: {
			dateFormat: Filters.dateFormat,
			scoreFormat:(v,f) => {return parseFloat(v).toFixed(2);}
	},

    mounted(){
        console.log("getsetting.....");
        this.getSetting();
    },
    methods: {
        handleSelectionChange: function(sels) {
				this.sels = sels;			
		},
        delOffOnTime(){
        
            this.devices = this.devices.filter(t => !this.sels.some(s => s.id === t.id))

        },
        addOffOnTime(){
            if(this.devices.length >= 4){
                console.log(this.devices.length);
                this.$message.error('最多只能添加4组拉合闸时间!');                
                return;
            }
            
            if(this.offTime === '' || this.onTime === ''){
                this.$message.error('拉合闸时间不能为空!');  
                return;
            }
            var id=this.devices.length+1;
            this.devices.push( {
                    offTime:Date.parse(this.offTime), 
                    onTime:Date.parse(this.onTime),  
                    deviceNo:this.deviceInfo[0].deviceNo,
                    deviceName:this.deviceInfo[0].deviceName,
                    id:id
                });
            
        },

        getSetting(){
             AdminAPI.getDeviceSetting({deviceNo:this.deviceInfo[0].deviceNo}).then(({
					data: jsonData
				}) => {
					if(jsonData !== null) {
						this.devices = jsonData;
					} else {
						this.$message({
							messsage: `获取设备拉合闸时间失败:${data.msg}`,
							type: 'error'
						})
					}
				});
        },

		batchEdidtSubmit() {            
             this.listLoading=true;
           
			this.$refs.batchEditForm.validate(valid => {
				if(valid) {		
                  
					AdminAPI.addDeviceSetting(                            
                                {ids:this.deviceInfo.map(item=>item.deviceNo),settings:this.devices}                           
                    ).then(({data}) => {
                        this.listLoading=false;
						if(data.status === 0) {
							this.$message({
								message: data.message
							});
							this.$emit('data', {});
							this.modalVisible = false;
						} else {
							this.$message.error(data.message);
						}
						
					}).catch(() => {
                        this.listLoading=false;
						this.$message.error('修改设备信息失败!');
					});
				}
			});
		},
    },
}
</script>

<style scoped lang="scss">
.edit-device-form {
    .el-dialog {
        width: 300px;
    }
    .el-dialog__body {
        padding-top: 5px;
    }
    .el-form--inline .el-form-item__content {
        width: 150px;
    }
    .el-tabs__header {
        margin-bottom: 0;
    }
}
</style>

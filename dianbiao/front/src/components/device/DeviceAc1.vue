<template>
		<section class="grid-content">
			<el-table :data="devices" resizable border highlight-current-row stripe v-loading="listLoading" ref="table" 
			 @selection-change="handleSelectionChange" 
			 @sort-change="handleSortChange"
			  class="cmcc-cell-nowrap">

				<el-table-column header-align="center"  type="selection">				
				</el-table-column>
				
				<el-table-column  sortable="custom" prop="roomName" label="机房" width="200"></el-table-column>
				

				<el-table-column  sortable="custom" prop="deviceName" label="设备名称" width="200"></el-table-column>
				
				<el-table-column  prop="deviceNo" label="设备编号" width="150" sortable="custom"></el-table-column>				
						
				<el-table-column  prop="status" label="设备状态" width="120" sortable="custom">
					<template slot-scope="scope">						
						<el-tag :type="scope.row.status == '0' ? 'success' : 'danger'" close-transition>{{scope.row.status == '0'?'在线':'离线'}}</el-tag>					
					</template>
				</el-table-column>

				<el-table-column  sortable="custom" prop="aState" label="开关" width="100">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.aState"
								 :active-value=0
								 :inactive-value=1
								 @change="onoffDevice($event,scope.row,1)"
								active-color="#13ce66" inactive-color="#ff4949" 
								style="float:left;"/>

						<el-button icon="el-icon-alarm-clock"   type="primary" circle
								class="branchsetting"
								v-bind:class="scope.row.branchSetting[1] === 1 ? 'hassetting':''"															
								@click="settingDevice(scope.row,1)"></el-button>
					</template>
				</el-table-column>
				
				
				<el-table-column  sortable="custom" prop="allEnergy" label="总电量(KW)" width="150"></el-table-column>
				
				<el-table-column  sortable="custom" prop="avol" label="电压" width="150"></el-table-column>
				
				<el-table-column  sortable="custom" prop="acur" label="电流"></el-table-column>
			
			
			</el-table>
		</section>
	
</template>

<script>
	

	export default {
        props:['devices'],
		data() {
			return {				
			}
		},		
		methods: {		
            handleSelectionChange(sels){                
                this.$emit('selection-change',sels);
            },	

            handleSortChange(col){                
                this.$emit('sort-change',col);
            },

            onoffDevice(p1,p2,p3){                
                 this.$emit('onoffDevice',p1, p2,p3);
            },

            settingDevice(p1,p2){
                 this.$emit('settingDevice',p1,p2);
            }
		}
		
	}
</script>



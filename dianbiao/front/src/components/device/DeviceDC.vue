<template>
	<section class="grid-content">
			<el-table :data="devices" resizable border highlight-current-row stripe 
                 ref="table" 
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

				<el-table-column  sortable="custom" prop="aState" label="1路开关" width="100">
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
					<el-table-column  sortable="custom" prop="bState" label="2路开关" width="110">
					<template slot-scope="scope">						
						<el-switch v-model="scope.row.bState"
								 :active-value=0
								 :inactive-value=1
								  @change="onoffDevice($event,scope.row,2)"
								active-color="#13ce66" inactive-color="#ff4949" />

							<el-button icon="el-icon-alarm-clock"   type="primary" circle
								class="branchsetting"
								v-bind:class="scope.row.branchSetting[2] === 1 ? 'hassetting':''"							
								@click="settingDevice(scope.row,2)"></el-button>
					</template>
				</el-table-column>
					<el-table-column  sortable="custom" prop="cState" label="3路开关" width="110">
					<template slot-scope="scope">						
						<el-switch v-model="scope.row.cState"
								 :active-value=0
								 :inactive-value=1
								  @change="onoffDevice($event,scope.row,3)"
								active-color="#13ce66" inactive-color="#ff4949" />

							<el-button icon="el-icon-alarm-clock"   type="primary" circle
								class="branchsetting"
								v-bind:class="scope.row.branchSetting[3] === 1 ? 'hassetting':''"								
								@click="settingDevice(scope.row,3)"></el-button>
					</template>
				</el-table-column>
					<el-table-column  sortable="custom" prop="dState" label="4路开关" width="110">
					<template slot-scope="scope">						
						<el-switch v-model="scope.row.dState"
								:active-value=0
								 :inactive-value=1
								@change="onoffDevice($event,scope.row,4)"
								active-color="#13ce66" inactive-color="#ff4949" />		
								
								<el-button icon="el-icon-alarm-clock"   type="primary" circle
								class="branchsetting"
								v-bind:class="scope.row.branchSetting[4] === 1 ? 'hassetting':''"						
								@click="settingDevice(scope.row,4)"></el-button>
					</template>
				</el-table-column>
				
				<el-table-column  sortable="custom" prop="allEnergy" label="总电量(KW)" width="150"></el-table-column>
				<el-table-column  sortable="custom" prop="aEnergy" label="1路电量(KW)" width="150"></el-table-column>
				<el-table-column  sortable="custom" prop="bEnergy" label="2路电量(KW)" width="150"></el-table-column>
				<el-table-column  sortable="custom" prop="cEnergy" label="3路电量(KW)" width="150"></el-table-column>
				<el-table-column  sortable="custom" prop="dEnergy" label="4路电量(KW)" width="150"></el-table-column>
			
				<el-table-column  sortable="custom" prop="avol" label="电压" width="150"></el-table-column>
				<!--el-table-column  sortable="custom" prop="bvol" label="2路电压" width="150"></el-table-column>
				<el-table-column  sortable="custom" prop="cvol" label="3路电压" width="150"></el-table-column>
				<el-table-column  sortable="custom" prop="dvol" label="4路电压" width="150"></el-table-column-->
			
				
				<el-table-column  sortable="custom" prop="acur" label="1路电流" width="150"></el-table-column>
				<el-table-column  sortable="custom" prop="bcur" label="2路电流" width="150"></el-table-column>
				<el-table-column  sortable="custom" prop="ccur" label="3路电流" width="150"></el-table-column>
				<el-table-column  sortable="custom" prop="dcur" label="4路电流" width="150"></el-table-column>
			
				
			
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



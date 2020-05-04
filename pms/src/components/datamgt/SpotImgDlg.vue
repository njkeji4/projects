<template>
	<!-- batch config -->
	<el-dialog title="现场图片" ref="modal"  width="630" :visible.sync="modalVisible" :close-on-click-modal="false"
         class="edit-device-form">
         
         <img :src="'data:image/jpg;base64,'+img"/>
		
		<div slot="footer" class="dialog-footer">
			<el-button size="small" @click="modalVisible = false">确定</el-button>			
		</div>
	</el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';
import { AdminAPI } from '../../api';

export default {
    data: function() {
       
        return {          
           modalVisible: true,
           
           img:'',
        };
    },
	computed: {
		
	},
    created() {                
        this.loadImg();
       
    },
    methods: {
		loadImg(){
            AdminAPI.getSpotImg(this.id).then(({
					data
				}) => {
					if(data.status === 0) {
						this.img = data.data.spotImg;										
					} else {
						this.$message({
							messsage: `获取数据失败:${data.message}`,
							type: 'error'
						})
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

var map = function(){
    return {
        backgroundColor: 'transparent',
        title: {
            text: '黔 南 设 备 分 布 情 况',
            subtext: '',
            sublink: '',
            left: 'center',
            textStyle: {
                color: '#fff'
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: '{b}',
            show:false,
        },
        legend: {
            orient: 'vertical',
            top: 'top',
            left: 'left',            
            textStyle: {
                color: '#fff'
            },
            data:['online','offline'],
            show:false
        },
        visualMap: {
            min: 1,
            max: 10,
            splitNumber: 2,
            color: ['green','red'],
            textStyle: {
                color: '#fff',
                show:false
            },
            show:false
        },
        geo: {
            map: 'qiannan',
            label: {
                show:true,
                color:'white',
                emphasis: {
                    show: false,
                    color:'white'
                }
            },
            itemStyle: {
                normal: {
                    areaColor: 'rgb(0,73,129)',
                    borderColor: 'rgb(2,152,206)'
                },
                emphasis: {
                    areaColor: 'rgb(0,73,129)',
                }
            }
        },
        series: [
            {
                name: 'online',
                type: 'scatter',
                coordinateSystem: 'geo',
                data: [
                    {name:'one', value:[107.736419707031,26.8329177070313,1]}                   
                ],
                symbolSize: 20,
                label: {
                    normal: {
                        formatter:'{b}',
                        color:'white',
                        position:'right',
                        show: false
                    },
                    emphasis: {
                        show: false,
                        formatter:'{b}',
                    }
                },
                itemStyle: {
                    emphasis: {
                        borderColor: '#fff',
                        borderWidth: 1
                    }
                }
            },
            {
                name: 'online',
                type: 'effectScatter',
                coordinateSystem: 'geo',
                data: [                    
                    {name:'two', value:[106.242139921875,25.7986794257813,10]}
                ],
                symbolSize: 20,
                label: {
                    normal: {
                        formatter:'{b}',
                        color:'white',
                        position:'right',
                        show: false
                    },
                    emphasis: {
                        show: false,
                        formatter:'{b}',
                    }
                },
                itemStyle: {
                    emphasis: {
                        borderColor: '#fff',
                        borderWidth: 1
                    }
                }
            }
        ]
    };
}

var pie = function(){
    return {
            legend: {
                orient: 'vertical',
                x: 'left',
                data:[],
                show:false
            },
            title:{
                text:'',
                top:'middle',
                left:'center',        
                textStyle:{
                    color:'white'
                }                 
            },
            color:['green','blue','red'],
            series: {
                type: 'pie',
                radius: ['60%', '80%'],
                avoidLabelOverlap: true,
                label: {
                    normal: {
                        show: true,
                        color:'white',
                        //position: 'inside',
                        formatter: '{b}({c})',
                    },
                    emphasis: {
                        show: false,
                        textStyle: {
                            fontSize: '30',
                            fontWeight: 'bold'
                        }
                    }
                },
                labelLine: {
                    normal: {
                        show: true
                    }
                },
                data: [
                
                ]
            }
        };
}

var bar = function(){
    return {
        title:{
            text:'认证情况一览',
            top:'bottom',
            left:'center',        
            textStyle:{
                color:'white'
            }                 
        },
        color: ['green', 'blue', 'red'],
        legend: {
            data: ['成功', '失败', '告警'],
            left:'center',
            top:'top',
            show:true,
            textStyle:{
                color:'white'
            }          
        },
        xAxis: {
            axisLine:{
                lineStyle:{
                    color:'white'
                },
            },
            type: 'category',
            data: ['group1', 'group2', 'group3']
        },
        yAxis: {
            axisLine:{
                lineStyle:{
                    color:'white'
                },
            },
            type: 'value'
        },
        series: [{
            name:'成功', 
            data: [120, 200, 150],
            type: 'bar'
        },
        {
            name:'失败',
            data: [120, 200, 150],
            type: 'bar'
        },
        
        {
            name:'告警',
            data: [120, 200, 150],
            type: 'bar'
        }]
    };
}

export default {
    map,
    pie,
    bar
};
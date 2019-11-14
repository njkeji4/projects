var map = function(){
    return {
        backgroundColor: 'transparent',
        title: {
            text: '黔南设备分布情况',
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
            top: 'bottom',
            left: 'right',
            data:['pm2.5'],
            textStyle: {
                color: '#fff'
            },
            show:false
        },
        visualMap: {
            min: 1,
            max: 10,
            splitNumber: 2,
            color: ['red','green'],
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
                name: '',
                type: 'scatter',
                coordinateSystem: 'geo',
                data: [
                    {name:'one', value:[107.736419707031,26.8329177070313,1]},
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
            color:['green','red','blue'],
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
            text:'按照组统计认证',
            top:'top',
            left:'center',        
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
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
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
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar'
        }]
    };
}

export default {
    map,
    pie,
    bar
};
import echarts from 'echarts';

var lines = function(){
    return {    
         grid: {
              x: 55,
              y: 35,
              x2: 15,
              y2: 35
          },
        color: ['#1cffea', '#006699', '#4cabce', '#e5323e'],
        legend: {
                  data: ['电量'],
                  show:false
          },
         xAxis: {
             axisLine:{
                 lineStyle:{
                     color:"#ffffff"
                 }
             },
             axisTick:{
                show:false
             },
              boundaryGap: false,
              type: 'category',
              data: ['1日', '2日', '3日', '4日', '5日', '6日', '7日']
          },
          yAxis: {
              axisLine:{
                 lineStyle:{
                     color:"#ffffff"
                 }
             },
             splitLine:{
                show:true,
                lineStyle:{
                     color:['#0F4E87'],
                    type:'dotted'
                }
            },
            axisTick:{
                show:false
            },
            type: 'value'
          },
          series: [{
              name:'电量',
              data: [120, 500, 450, 120, 300, 500, 600],
              type: 'line',
               areaStyle: {normal: {
                  color: 
                  new echarts.graphic.LinearGradient(1, 0, 0, 0, [
                        {offset: 0, color: '#9B00C5'},
                        {offset: 0.5, color: '#584BC2'},
                        {offset: 1, color: '#00A1D0'}
                   
                  ]),
              }}
           }]
};
}

var bar = function(){return {
          color: ['#1cffea', '#006699', '#4cabce', '#e5323e'],
          grid: {
              x: 35,
              y: 35,
              x2: 30,
              y2: 35
          },
          legend: {
                  data: ['能耗'],
                  show:false
          },
          xAxis: {
               splitLine: {
                  show:false,
              },
              axisLabel: {
                      show: true,
                      textStyle: {
                          color: '#c3eaff'
                      }
              },
              axisLine:{
                 lineStyle:{
                     color:"#0B50AC"
                 }
             },
             axisTick:{
                  show:false
             },
              type: 'category',
              data: ['1月', '2月', '3月', '4月', '5月', '6月','6月','6月']
          },
          yAxis: {
              axisLine:{
                 lineStyle:{
                     color:"#0B50AC"
                 }
              },
              axisTick:{
                  show:false
              },
              splitLine:{
                  show:true,
                  lineStyle:{
                       color:['#0F4E87'],
                      type:'dotted'
                  }
              },
              type: 'value',
              
               axisLabel: {
                      show: true,
                      textStyle: {
                          color: '#c3eaff'
                      }
              },
          },
          series: [
              {
                  name:'电量',
                  data: [950, 800, 500, 800, 500, 400,210,150],
                  type: 'bar',
                  itemStyle: {			// 颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
                      color: new echarts.graphic.LinearGradient(
                          0, 0, 0, 1,     //代表从正上方开始
                          [
                              {offset: 0, color: '#0CC8FF'},
                              
                              {offset: 1, color: '#258FFF'}
                          ]
                      )
                  },
                  barWidth: 20,
              }
          ]
};
}

var bar2 = function(){return {
    color: ['#1cffea', '#006699', '#4cabce', '#e5323e'],
    grid: {
        x: 35,
        y: 15,
        x2: 30,
        y2: 35
    },
    legend: {
            data: ['能耗'],
            show:false
    },
    xAxis: {
         splitLine: {
            show:false,
        },
        axisLabel: {
                show: true,
                textStyle: {
                    color: '#c3eaff'
                }
        },
        axisLine:{
           lineStyle:{
               color:"#0B50AC"
           }
       },
       axisTick:{
            show:false
       },
        type: 'value',
       
    },
    yAxis: {
        data: ['1月', '2月', '3月', '4月', '5月', '6月'],
        axisLine:{
           lineStyle:{
               color:"#0B50AC"
           }
        },
        axisTick:{
            show:false
        },
        splitLine:{
            show:false,
        },
        type: 'category',
        
         axisLabel: {
                show: true,
                textStyle: {
                    color: '#c3eaff'
                }
        },
    },
    series: [
        {
            name:'电量',
            data: [950, 800, 500, 800, 500, 400],
            type: 'bar',
            itemStyle: {			// 颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
                color: new echarts.graphic.LinearGradient(
                    1, 0, 0, 0,     //代表从正上方开始
                    [
                        {offset: 0, color: '#258FFF'},
                        
                        {offset: 1, color: '#0CFFF5'}
                    ]
                )
            },
            barWidth: 8,
        }
    ]
};
}

var pie = function(){return {
 
                  legend: {
                      textStyle:{
                          color:'#7df5f9',
                          fontFamily:'MicrosoftYaHei',
                          fontSize:10
                      },
                      orient: 'vertical',
                      right:20,
                      top:10,
                      
                      formatter: '{name}'
                  },
                  title:{
                    text:'',
                    top:'top',
                    left:'right',                         
                  },
                  series: {
                      color:['#0E59FF','#27ECDC','#CC00FD','#FF73B8','#A5EF67','#FCA24A'],
                      type: 'pie',
                      radius: ['60%', '80%'],
                      avoidLabelOverlap: true,
                      center:['30%','50%'],
                      label: {
                          color:'#fff',
                          normal: {
                              show: false,
                              position: 'inside',
                              formatter: '{c}',
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
                              show: false
                          }
                      },
                      data: [
                          {name:"一组   862KW",value:862},
                          {name:"二组   792KW",value:792},
                          {name:"三组   626KW",value:626},
                          {name:"四组   708KW",value:708},
                          {name:"五组   543KW",value:543},
                        
                      ]
                  }
};}

var nestedBar = function(){return {
    color: [
            new echarts.graphic.RadialGradient(0.5, 0.5, 1.0, [{
                    offset: 0,
                    color: '#040813'
                }, {
                    offset: 1,
                    color: 'blue'
                }]), 
                
            new echarts.graphic.LinearGradient(1, 0, 0, 0, [
                    {offset: 0, color: '#31D6CE'},
                    {offset: 1, color: '#3B80E9'}
            ]), 
            
            '#020911', '#020911'],

    grid: {
        x: 35,
        y: 15,
        x2: 30,
        y2: 35
    },        

    series: [
        {
            center:['30%','50%'],
            name: '访问来源',
            type: 'pie',
            selectedMode: 'single',
            radius: [0, '60%'],

            label: {
                color:'#fff',
                position: 'center',
                verticalAlign:'top'
            },
            labelLine: {
                show: false
            },
            data: [
                {value: 100, name: '节能比\n\n55%'},
                {value: 0, name: ''},
                {value: 0, name: ''}
            ]
        },
        {
            center:['30%','50%'],
            name: '访问来源',
            type: 'pie',
            radius: ['70%', '85%'],
            label: {
               show:false
            },
            data: [
                {value: 22, name: ''},
                {value: 55, name: ''},
                {value: 23, name: ''},
            ]
        }
    ]
};
}

export default {
    lines,pie,bar,bar2,nestedBar
};

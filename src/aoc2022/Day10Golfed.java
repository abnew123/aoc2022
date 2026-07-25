package aoc2022;
class J{String s(boolean p,String[]I){int x=1,c=0,a=0,y,g[]=new int[8];for(var l:I)for(var t:l.split(" ")){a+=c%40==19?-~c*x:0;g[c/5%8]+=g[c/5%8]+((y=x-c++%40)*y<2?1:0);x+=t.charAt(0)<97?new Integer(t):0;}var s="";for(int v:g)s+="AGZRBEFHPLKUAJC".charAt(v%2348%15);return p?""+a:s;}}

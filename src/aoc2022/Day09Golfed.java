package aoc2022;
class I{String s(boolean p,String[]I){int n=p?4:20,q[]=new int[n];var v=$.s();v.add(0L);for(var l:I){int d="LRDU".indexOf(l.charAt(0)),m=$.i(l.substring(2));for(;m-->0;){q[d/2]+=d%2*2-1;for(int i=0;(i+=2)<n;){int a=q[i-2]-q[i],b=q[i-1]-q[i+1];if(a*a+b*b>2){q[i]+=$.g(a);q[i+1]+=$.g(b);}}v.add((long)q[n-2]<<32^q[n-1]);}}return""+v.size();}}

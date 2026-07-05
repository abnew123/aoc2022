package aoc2022;class F{String s(boolean p,String x){int n=p?4:14,i=0,m,j;for(;i<x.length();i++){m=0;for(j=0;j<n;)m|=1<<x.charAt(i+j++);if($.b(m)==n)return""+(i+n);}return"";}}

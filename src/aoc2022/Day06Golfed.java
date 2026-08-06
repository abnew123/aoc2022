package aoc2022;class F{String s(boolean p,String[]x){int n=p?4:14,i=0;while(x[0].substring(i,i+n).matches(".*(.).*\\1.*"))i++;return i+n+"";}}

package aoc2022;class B{String s(boolean p,String x){int r=0,a,b;for(String l:x.split("\n")){a=l.charAt(0)-65;b=l.charAt(2)-88;r+=p?b+1+(b-a+4)%3*3:b*3+(a+b+2)%3+1;}return""+r;}}

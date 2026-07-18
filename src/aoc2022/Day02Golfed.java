package aoc2022;class B{String s(boolean p,String[]x){int r=0,a,b;for(var l:x){a=l.charAt(0);b=l.charAt(2)%4;r+=p?b+1+(b-a+69)%3*3:b*3+(a+b)%3+1;}return""+r;}}

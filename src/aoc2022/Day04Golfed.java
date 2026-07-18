package aoc2022;class D{String s(boolean p,String[]x){int r=0;for(var l:x){var s=$.c(l);int A=s.nextInt(),B=s.nextInt(),C=s.nextInt(),D=s.nextInt();if(p?1L*(A-C)*(B-D)<1:A<=D&C<=B)r++;}return""+r;}}

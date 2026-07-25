package aoc2022;
class L{String s(boolean p,String[]L){int C=L[0].length(),N=L.length*C,S=0,h[]=new int[N],v[]=h.clone(),i,n,d=1;for(i=N;i-->0;){var c=L[i/C].charAt(i%C);h[i]=c>90?c-97:c<70?(v[i]=1)*25:(S=i)*0;}for(;;d++)for(i=N;i-->0;)if(v[i]==d){if(p?i==S:h[i]<1)return~-d+"";for(int m:new int[]{1,-1,C,-C})if((n=i+m)>=0&n<N&&v[n]<1&h[n]>h[i]-2&(m*m>1|i/C==n/C))v[n]=d+1;}}}

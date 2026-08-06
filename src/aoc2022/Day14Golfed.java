package aoc2022;
import java.util.*;
class N extends HashSet{int m,y;String s(boolean p,String[]I){clear();m=y=0;for(var l:I){var s=new Scanner(l).useDelimiter("\\D+");for(int a=s.nextInt(),b=s.nextInt();s.hasNext();)for(int c=s.nextInt(),d=s.nextInt();add(a+","+(b>m?m=b:b))|a!=c|b!=d;a+=c>a?1:c<a?-1:0,b+=d>b?1:d<b?-1:0);}int A=0,x=500,X;for(;!p|++y<=m;)if(f(X=x)||f(--X)||f(X+=2))x=X;else{add(x+","+--y);A++;if(y<1)break;x=500;y=0;}return""+A;}boolean f(int x){return y<m+2&!contains(x+","+y);}}

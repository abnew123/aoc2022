package aoc2022;
import java.util.*;
class N extends HashSet{int m,y;String s(boolean p,String[]I){clear();m=y=0;for(var l:I){var s=new Scanner(l).useDelimiter("\\D+");int a=s.nextInt(),b=s.nextInt();while(s.hasNext()){int c=s.nextInt(),d=s.nextInt();for(;;){add(a+","+b);m=m<b?b:m;if(a==c&b==d)break;a+=c>a?1:c<a?-1:0;b+=d>b?1:d<b?-1:0;}}}int A=0,x=500,X;for(;;){y++;if(f(X=x)||f(--X)||f(X+=2)){if(p&y>m)return""+A;x=X;}else{add(x+","+--y);A++;if(y<1)return""+A;x=500;y=0;}}}boolean f(int x){return y<m+2&!contains(x+","+y);}}

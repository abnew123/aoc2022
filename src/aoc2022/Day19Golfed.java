package aoc2022;
class S{int q[]=new int[12],z;
String s(boolean p,String[]I){
int r=p?0:1,j=0,t=p?24:32;
for(var l:I)if(j++<3|p){var x=l.split("\\D+");for(int y=6;y-->0;)q[y<3?y*3:y*2+1]=new Integer(x[y+2]);d(t,1,0,0,0,0,0,0,z=0);r=p?r+new Integer(x[1])*z:r*z;}
return""+r;
}
void d(int t,int...s){
for(int i=(z=z<s[7]?s[7]:z)<s[7]+t*t/2?4:0;i-->0;){
int w=0,j,y;
for(j=3;j-->0;)if((y=q[i*3+j]-s[4+j])>0&w<(y=s[j]<1?t:~-y/s[j]+1))w=y;
if(w++<t){var u=s.clone();u[i]++;u[7]+=i/3*(t-w);for(j=3;j-->0;)u[4+j]+=s[j]*w-q[i*3+j];d(t-w,u);}
}
}
}

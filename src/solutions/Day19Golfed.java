
class S{int q[]=new int[14],z;
String s(boolean p,String[]I){
int j=0,t=p?24:32,r=t>>5;
for(var l:I)if(j++<3|p){for(int y=8;y-->1;)q[y<5?y*3-6&15:y*2-3]=new Integer(l.split("\\D+")[y]);d(t,1,0,0,0,0,0,0,z=0);r=p?r+q[13]*z:r*z;}
return""+r;
}
void d(int t,int...s){
for(int i=(z=z<s[7]?s[7]:z)<s[7]+t*t/2?4:0;i-->0;){
int w=0,j,y;
for(j=3;j-->0;w=(y=q[i*3+j]-s[4+j])>0&w<(y=s[j]<1?t:~-y/s[j]+1)?y:w);
if(w++<t){var u=s.clone();u[i]++;u[7]+=i/3*(t-w);for(j=3;j-->0;)u[4+j]+=s[j]*w-q[i*3+j];d(t-w,u);}
}
}
}

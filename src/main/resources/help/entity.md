
    
>  Demo
```
@NotNull(message = "名字不能为空")
private String name;

@Size(min = 6,max = 30,message = "地址应该在6-30字符之间")
private String address;

@DecimalMax(value = "100.00",message = "体重有些超标哦")
@DecimalMin(value = "60.00",message = "多吃点饭吧")
private BigDecimal weight;

private String friendName;
@AssertTrue
private Boolean isHaveFriend(){
    return friendName != null?true:false;
}

@Future(message = "生日必须在当前实践之前")
private Date birthday;

@Pattern(regexp = "^(.+)@(.+)$",message = "邮箱的格式不合法")
private String email;
```



> @AssertFalse


```
Boolean, boolean

验证注解的元素值是false
```

> @AssertTrue


```
Boolean, boolean

验证注解的元素值是true
```

> @DecimalMax（value=x）


```
BigDecimal, BigInteger, String, byte,short, int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type of Number andCharSequence.

验证注解的元素值小于等于@ DecimalMax指定的value值
```

> @DecimalMin（value=x）


```
BigDecimal, BigInteger, String, byte,short, int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type of Number andCharSequence.

验证注解的元素值小于等于@ DecimalMin指定的value值
```

> @Digits(integer=整数位数, fraction=小数位数)


```
BigDecimal, BigInteger, String, byte,short, int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type of Number andCharSequence.

验证注解的元素值的整数位数和小数位数上限
```


> @Future


```
java.util.Date, java.util.Calendar; Additionally supported by HV, if theJoda Time date/time API is on the class path: any implementations ofReadablePartial andReadableInstant.

验证注解的元素值（日期类型）比当前时间晚
```


> @Max（value=x）


```
BigDecimal, BigInteger, byte, short,int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type ofCharSequence (the numeric value represented by the character sequence is evaluated), any sub-type of Number.

验证注解的元素值小于等于@Max指定的value值
```

> @Min（value=x）


```
BigDecimal, BigInteger, byte, short,int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type of CharSequence (the numeric value represented by the char sequence is evaluated), any sub-type of Number.

验证注解的元素值大于等于@Min指定的value值
```

> @NotNull


```
Any type

验证注解的元素值不是null
```


> @Null


```
Any type

验证注解的元素值是null
```


> @Past

```
java.util.Date, java.util.Calendar; Additionally supported by HV, if theJoda Time date/time API is on the class path: any implementations ofReadablePartial andReadableInstant.

验证注解的元素值（日期类型）比当前时间早
```


> @Pattern(regex=正则表达式, flag=)

```
String. Additionally supported by HV: any sub-type of CharSequence.

验证注解的元素值与指定的正则表达式匹配
```


> @Size(min=最小值, max=最大值)


```
String, Collection, Map and arrays. Additionally supported by HV: any sub-type of CharSequence.

验证注解的元素值的在min和max（包含）指定区间之内，如字符长度、集合大小
```


> @Valid


```
Any non-primitive type（引用类型）

验证关联的对象，如账户对象里有一个订单对象，指定验证订单对象
```


> @NotEmpty


```
CharSequence,Collection, Map and Arrays

验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0）
```


> @Range(min=最小值, max=最大值)


```
CharSequence, Collection, Map and Arrays,BigDecimal, BigInteger, CharSequence, byte, short, int, long and the respective wrappers of the primitive types

验证注解的元素值在最小值和最大值之间
```

> @NotBlank


```
CharSequence

验证注解的元素值不为空（不为null、去除首位空格后长度为0），不同于@NotEmpty，@NotBlank只应用于字符串且在比较时会去除字符串的空格
```


> @Length(min=下限, max=上限)


```
CharSequence

验证注解的元素值长度在min和max区间内
```


> @Email

```
CharSequence

验证注解的元素值是Email，也可以通过正则表达式和flag指定自定义的email格式
```

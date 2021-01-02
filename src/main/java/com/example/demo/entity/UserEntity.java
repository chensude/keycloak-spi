package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



/**
 * @author <a href="mailto:bbalasub@redhat.com">Bala B</a>
 * @version $Revision: 1 $
 */

@Data
@TableName("user_store")
public class UserEntity {

	private String userName;
	private Integer id;
	private String email;
	private String phone;
	private String fullName;
}

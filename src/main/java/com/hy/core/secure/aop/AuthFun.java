/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hy.core.secure.aop;

import com.hy.common.tool.Utils;
import com.hy.core.secure.utils.TokenUtil;

public class AuthFun {

	/**
	 * 放行所有请求
	 *
	 * @return {boolean}
	 */
	public boolean permitAll() {
		return true;
	}

	/**
	 * 只有超管角色才可访问
	 *
	 * @return {boolean}
	 */
	public boolean denyAll() {
		return hasRole("admin");
	}

	/**
	 * 判断是否有该角色权限
	 *
	 * @param role 单角色
	 * @return {boolean}
	 */
	public boolean hasRole(String role) {
		return hasAnyRole(role);
	}

	/**
	 * 判断是否有该角色权限
	 *
	 * @param requireRoles 角色集合
	 * @return {boolean}
	 */
	public boolean hasAnyRole(String... requireRoles) {
		String cachedUserRole = TokenUtil.extractTokenStub().getRoleNames();
		if (Utils.isBlank(cachedUserRole)) {
			return false;
		}
		String[] userRoles = Utils.toStrArray(cachedUserRole);
		for (String requireRole : requireRoles) {
			if (Utils.contains(userRoles, requireRole)) {
				return true;
			}
		}
		return false;
	}

}

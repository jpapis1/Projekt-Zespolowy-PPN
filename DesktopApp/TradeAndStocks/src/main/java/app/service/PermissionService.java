/*
 *    Copyright 2018-2019 Jacek Papis, Michał Piątek
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    Data provided for free by IEX https://iextrading.com/developer/.
 *    View IEX’s Terms of Use https://iextrading.com/api-exhibit-a/.
 */

package app.service;

import app.model.Permission;
import app.model.PermissionEnum;
import app.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public Permission getPermissionByEnum(PermissionEnum name) {
        return permissionRepository.findFirstByName(name);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}

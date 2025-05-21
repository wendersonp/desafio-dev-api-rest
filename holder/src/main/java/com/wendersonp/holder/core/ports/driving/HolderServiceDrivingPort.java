package com.wendersonp.holder.core.ports.driving;

import com.wendersonp.holder.core.exceptions.BusinessException;
import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.model.HolderRequestModel;

public interface HolderServiceDrivingPort {

    HolderModel save(HolderRequestModel holderModel);

    HolderModel findByDocument(String documentNumber) throws BusinessException;
}

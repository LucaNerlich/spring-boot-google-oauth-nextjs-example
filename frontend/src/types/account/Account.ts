import type {AbstractEntity} from "@/types/AbstractEntity";
import type {ConnectedAccount} from "@/types/account/ConnectedAccount";

export interface Account extends AbstractEntity {
    imageUrl: string | undefined,
    name: string,
    banned: boolean,
    connectedAccounts: ConnectedAccount[],
    email: string
}


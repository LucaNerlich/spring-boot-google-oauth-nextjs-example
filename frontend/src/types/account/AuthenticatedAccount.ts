import type {AbstractEntity} from "@/types/AbstractEntity";
import type {ConnectedAccount} from "@/types/account/ConnectedAccount";

interface Account extends AbstractEntity {
    imageUrl: string | undefined,
    name: string,
}

export interface AuthenticatedAccount extends Account {
    banned: boolean,
    connectedAccounts: ConnectedAccount[],
    email: string
}
